setlocal ENABLEDELAYEDEXPANSION 
set COUNT=0
for /F %i in (unique_rex.txt) do (
set /A COUNT=!COUNT! + 1
Rex /dot:!COUNT!.dot %i
)
