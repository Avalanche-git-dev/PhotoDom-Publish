# echo "Deleting Kind Cluster photo-dom"
# kind delete cluster --name photodom
# echo "Kind Cluster photo-dom Deleted"



Write-Output "=== Inizio cancellazione cluster Kind PhotoDom ==="

# Funzione per rimuovere in sicurezza una entry dal file hosts
function Remove-HostEntry {
    param (
        [string]$hostname
    )

    $hostsFile = "C:\Windows\System32\drivers\etc\hosts"
    $content = Get-Content -Path $hostsFile

    $linesToRemove = $content | Where-Object { $_ -match "$hostname" }

    if ($linesToRemove) {
        $filteredContent = $content | Where-Object { $_ -notmatch "$hostname" }
        $filteredContent | Set-Content -Path $hostsFile -Encoding ascii -Force
        Write-Output "   Rimosso: $hostname dal file hosts."
    }
    else {
        Write-Output "    Nessuna riga trovata per $hostname. Nessuna modifica effettuata."
    }
}

# Cancella il cluster Kind
Write-Output "Cancellazione del Kind cluster 'photodom'..."
kind delete cluster --name photodom
Write-Output "Kind cluster 'photodom' eliminato."

# Pulizia file hosts
Write-Output "Pulizia file hosts..."
Remove-HostEntry -hostname "keycloak"
Remove-HostEntry -hostname "api-gateway"

Write-Output "Pulizia completata!"

Write-Output "=== Fine cancellazione cluster ==="
