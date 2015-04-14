// To have access to the API from Python script
import understand

// To load a UDB file into memory
db = understand.open(dbfile)

// To 	access info on .CPP files
for file in (db.lookup(".cpp","File")):

	// To make sure only classes are being picked up
	if((ent.kindname() == "Class") or (ent.kindname() == "Public Class") or (ent.kindname() == "Unknown Class") or (ent.kindname() == "Private Class")):

	// To get CBO/LCOM for a class. -1 is used as error code.
	metric = file.metric(("CountClassCoupled",))
	if metric["CountClassCoupled"] is not None:
		return metric["CountClassCoupled"]
