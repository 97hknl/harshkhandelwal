#
# Test script for AP1
#
# Platform: Windows 8, 10
# Author:   Thomas Pfann
#

New-Item -Path ./expressions.in.txt -Force | Out-Null;
New-Item -Path ./expressions.out.txt -Force | Out-Null;
python util\generate_expressions.py > expressions.in.txt;
$content = Get-Content .\expressions.in.txt

foreach($line in $content)
{
    $output = "$line = "
    $output += Invoke-Expression $line | Tee-Object .\expressions.out.txt -Append
    Write-Output "$output" 
} 

Write-Output "`nYour answers: `n"

cat .\expressions.in.txt | java -jar .\build\libs\AP1-1.0.jar
