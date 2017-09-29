# Name:
#	rfid_reader.ps1
# By:
#	Peter Wolfe, wolfe@pobox.com
#
# Description:
#	Set up environment to run the mighty FRC2590 RFID reader program

$current_dir = Get-Location
#write-host "$current_dir"

# The directory structure for the app is:
#     rfid_reader
#      bin/rfid_reader
#      data
#      lib
# The required java command line is:
#     java -classpath "bin;lib\*" rfid_reader.RFIDreader

java -classpath "bin;lib\*" rfid_reader.RFIDreader
