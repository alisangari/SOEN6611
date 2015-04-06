#Assignment 3

import understand
import sys

#get all .cpp files
def getAllCppFiles(db):
	cppFiles = []
	for file in (db.lookup(".cpp","File")):
		cppFiles.append(file.name())
		#print(file.name())
	return cppFiles


#get all class type(s) enteties with the same name as .cpp files
def getValidCppClasses(db, cppFiles):
	cppClasses = []
	cppFilesWithoutExt = []
	for fileName in cppFiles:
		cppFilesWithoutExt.append(fileName[:-4])
	for ent in sorted(db.ents(),key= lambda ent: ent.name()):
		if((ent.kindname() == "Class") or (ent.kindname() == "Public Class") or (ent.kindname() == "Unknown Class") or (ent.kindname() == "Private Class")):
			if(ent.name() in cppFilesWithoutExt):
				cppClasses.append(ent.name())
				#print(ent.name())
	return cppClasses
	

#get cbo for class. Using the metric provided by Understand
def getCBO(db, className, classType):
	for file in (db.lookup(className,classType)):
		metric = file.metric(("CountClassCoupled",))
		if metric["CountClassCoupled"] is not None:
			return metric["CountClassCoupled"]


#get lcom for class. Using the metric provided by Understand
def getLCOM(db, className, classType):
	for file in (db.lookup(className,classType)):
		metric = file.metric(("PercentLackOfCohesion",))
		if metric["PercentLackOfCohesion"] is not None:
			return metric["PercentLackOfCohesion"]


#get class path
def getClassPath(db, className):
	for file in (db.lookup(className+".cpp",'File')):
		return file.longname()
	
	
#get/calc cbo and lcom for class enteties
def getReleaseInfo(db, cppClasses):
	thisRelease = ["release,clss_path,cbo,lcom"]
	#thisRelease = []
	release = "latest version"
	classPath = ""
	cbo = ""
	lcom = ""	
	for cppClass in cppClasses:
		for ent in sorted(db.ents(),key= lambda ent: ent.name()):
			if( ((ent.kindname() == "Class") or (ent.kindname() == "Public Class") or (ent.kindname() == "Unknown Class") or (ent.kindname() == "Private Class")) and (ent.name() == cppClass)):
				classPath = getClassPath(db,cppClass)
				cbo = getCBO(db, cppClass, ent.kindname())
				lcom = getLCOM(db, cppClass, ent.kindname())
				if (cbo is None):
					cbo = "-1"
				if (lcom is None):
					lcom = "-1"
				thisRelease.append(release+","+classPath+","+str(cbo)+","+str(lcom))
				break
	return thisRelease

	
#write it all to file
def writeReleaseInfo(releaseInfo, ouputFileName):
	with open(ouputFileName, "wt") as out_file:
		for record in releaseInfo:
			print (record,sep="",end="\n", file=out_file)
	out_file.close()





if __name__ == '__main__':
	# Open Database. udb file is provided as an argument.
	# ie. assignment3 Chromium.udb
	args = sys.argv
	
	print("Opening database...")
	db = understand.open(args[1])

	print("Compiling a list of all cpp class files...")
	cppFiles = getAllCppFiles(db)
	cppClasses = getValidCppClasses(db, cppFiles)

	print("Extracting CBO and LCOM...")
	releaseInfo = getReleaseInfo(db,cppClasses)
	
	print("Generating output file...")
	ouputFileName = "assignment3.csv"
	writeReleaseInfo(releaseInfo, ouputFileName)

	# **** repeat this for all versions
	
	print("Done!")

	