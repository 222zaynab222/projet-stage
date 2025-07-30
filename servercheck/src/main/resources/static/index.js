let lastCheckData = null;
function checkMatricule() {
    const input = document.getElementById('matriculeInput').value.trim();
    const resultDiv = document.getElementById('result');
    const addBtn = document.getElementById('showAddBtn');
    const addForms = document.getElementById('addForms');
    resultDiv.className = 'result';
    resultDiv.classList.remove('success', 'error', 'info', 'visible');
    addBtn.classList.add('hidden');
    addForms.classList.add('hidden');
    if (!input) {
        resultDiv.textContent = "Please enter a server name.";
        resultDiv.className = "result visible error";
        return;
    }
    fetch(`/check?serverName=${encodeURIComponent(input)}`)
        .then(res => res.json())
        .then(data => {
            lastCheckData = data;
            if (data.error) {
                resultDiv.textContent = "Error: " + data.error;
                resultDiv.className = "result visible error";
            } else if (data.hasAAS && data.hasAPS) {
                resultDiv.textContent = `Server '${data.serverName}' APS AND AAS are connecté et on dispose de ${data.apsCount} APS.`;
                resultDiv.className = "result visible success";
            } else if (data.hasAAS) {
                resultDiv.textContent = `Server '${data.serverName}' uniquement connecté au AAS et non au APS.`;
                resultDiv.className = "result visible info";
                addBtn.classList.remove('hidden');
            } else if (data.hasAPS) {
                resultDiv.textContent = `Server '${data.serverName}' uniquement connecté au APS et non au AAS.`;
                resultDiv.className = "result visible info";
                addBtn.classList.remove('hidden');
            } else {
                resultDiv.textContent = `Server '${data.serverName}' n'existe pas.`;
                resultDiv.className = "result visible error";
                addBtn.classList.remove('hidden');
            }
        });
}
function showAddForms() {
    const addForms = document.getElementById('addForms');
    addForms.classList.remove('hidden');
    // Prefill server names if possible
    const input = document.getElementById('matriculeInput').value.trim();
    document.getElementById('aasServerInput').value = input;
    document.getElementById('apsServerInput').value = input;
    updateAPSServerNames();
    // Scroll the glass container to the bottom
    const glass = document.querySelector('.glass');
    if (glass) {
        glass.scrollTo({ top: glass.scrollHeight, behavior: 'smooth' });
    }
}
// --- Add AAS ---
function addAAS() {
    const server = document.getElementById('aasServerInput').value.trim();
    const resultDiv = document.getElementById('aasResult');
    resultDiv.textContent = '';
    resultDiv.className = '';
    if (!server) {
        resultDiv.textContent = 'Please enter a server name.';
        resultDiv.className = 'result visible error';
        return;
    }
    fetch(`/addAAS?serverName=${encodeURIComponent(server)}`, {method:'POST'})
        .then(res => res.json())
        .then(data => {
            if (data.success) {
                resultDiv.textContent = `AAS for '${server}' added!`;
                resultDiv.className = 'result visible success';
            } else {
                resultDiv.textContent = data.error ? `Error: ${data.error}` : 'Failed to add AAS.';
                resultDiv.className = 'result visible error';
            }
        });
}
// --- Add APS ---
function addAPSRow() {
    const apsRows = document.getElementById('apsRows');
    const idx = apsRows.children.length;
    const row = document.createElement('div');
    row.className = 'input-row';
    row.innerHTML = `
            <span style="font-size:0.98em; color:#888;">presentation.<span id='apsServerName${idx}'></span>.</span>
            <input type="text" placeholder="part1" style="width:22%;" id="apsPart1_${idx}">
            <span style="font-size:0.98em; color:#888;">.</span>
            <input type="text" placeholder="part2" style="width:22%;" id="apsPart2_${idx}">
            <button type="button" onclick="this.parentNode.remove();" style="background:#ffeaea; color:#b00020; padding:0.4em 1em; font-size:0.95em;">Remove</button>
        `;
    apsRows.appendChild(row);
    updateAPSServerNames();
}
function updateAPSServerNames() {
    const server = document.getElementById('apsServerInput').value.trim();
    const apsRows = document.getElementById('apsRows');
    for (let i = 0; i < apsRows.children.length; i++) {
        const span = apsRows.children[i].querySelector(`#apsServerName${i}`);
        if (span) span.textContent = server || 'server';
    }
}
document.getElementById('apsServerInput').addEventListener('input', updateAPSServerNames);
// Add one APS row by default
if (document.getElementById('apsRows').children.length === 0) addAPSRow();
function addAPS() {
    const server = document.getElementById('apsServerInput').value.trim();
    const resultDiv = document.getElementById('apsResult');
    resultDiv.textContent = '';
    resultDiv.className = '';
    if (!server) {
        resultDiv.textContent = 'Please enter a server name.';
        resultDiv.className = 'result visible error';
        return;
    }
    const apsRows = document.getElementById('apsRows');
    const suffixes = [];
    for (let i = 0; i < apsRows.children.length; i++) {
        const part1 = apsRows.children[i].querySelector(`#apsPart1_${i}`).value.trim();
        const part2 = apsRows.children[i].querySelector(`#apsPart2_${i}`).value.trim();
        if (!part1 || !part2) continue;
        suffixes.push({part1, part2});
    }
    if (suffixes.length === 0) {
        resultDiv.textContent = 'Please enter at least one APS entry (both parts).';
        resultDiv.className = 'result visible error';
        return;
    }
    fetch(`/addAPS?serverName=${encodeURIComponent(server)}`, {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({suffixes})
    })
        .then(res => res.json())
        .then(data => {
            if (data.successCount && data.successCount > 0) {
                resultDiv.textContent = `${data.successCount} APS entr${data.successCount > 1 ? 'ies' : 'y'} added for '${server}'!`;
                resultDiv.className = 'result visible success';
            } else {
                resultDiv.textContent = data.error ? `Error: ${data.error}` : 'Failed to add APS.';
                resultDiv.className = 'result visible error';
            }
        });
}