# Name:
#	rfid_reader.ps1
# By:
#	Peter Wolfe, wolfe@pobox.com
#
# Description:
#	Set up environment to run the mighty FRC2590 RFID reader program

# PSScriptRoot is the directory from which we are executing
# (where this script lives! the top level of our 
# installation tree  - not the process's PWD)
# However, we look for all input data files relative to 
# our installation directy so we need to set and restore
# the user's PWD
$starting_dir = Get-Location 


try 
{
    # The directory structure for the app is:
    #     rfid_reader
    #      bin/rfid_reader
    #      data
    #      lib
    # The required java command line is:
    #     java -classpath "bin;lib\*" rfid_reader.RFIDreader

    $cp = $PSScriptRoot + "\bin;" + $PSScriptRoot + "\lib\*"

    Set-location $PSScriptRoot  # cd to execution dir

    #java -classpath "bin;lib\*" rfid_reader.RFIDreader
    java -classpath $cp rfid_reader.RFIDreader
}
finally
{
    # write-host "here: $current_dir"
    Set-Location $starting_dir
}