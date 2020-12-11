<?php

define('DB_HOST','localhost');
define('DB_USERNAME','root');
define('DB_PASSWORD','');
define('DB_NAME', 'placements');


//Connecting to the database
$conn = new mysqli(DB_HOST, DB_USERNAME, DB_PASSWORD, DB_NAME);

//checking the successful connection
if($conn->connect_error) {
die("Connection failed: " . $conn->connect_error);
}

//making an array to store the response
$response = array();

if($_SERVER['REQUEST_METHOD']=='POST'){

	$received_Array = $_POST['array'];
	$jsonArray = json_decode($received_Array, true);

	$query = "SELECT * FROM placementTable";
	$result = mysqli_query($conn, $query);
	$response["new_data"] = array();

	if(mysqli_num_rows($result) >0) {
		while ($row = mysqli_fetch_array($result)) {
			$temp = array();
			$tempDesc = array();
			$temp["PlacementID"] = $row["PlacementID"];
			$temp["PlacementName"] = $row["PlacementName"];
			$temp["Company"] = $row["Company"];
			$temp["Deadline"] = $row["Deadline"];
			$temp["Type"] = $row["Type"];
			$temp["Salary"] = $row["Salary"];
			$temp["Location"] = $row["Location"];
			$temp["Description"] = utf8_encode($row["Description"]);
			$temp["Subject"] = $row["Subject"];
			$temp["Miles"] = $row["Miles"];
			$temp["Paid"] = $row["Paid"];
			$temp["URL"] = $row["URL"];
			array_push($response["new_data"], $temp);
		}

		foreach ($jsonArray as $row) {
			$ID = utf8_decode($row['placementID']);
		    $name = utf8_decode($row['placementName']);
		    $company = utf8_decode($row['company']);
		    $deadline = utf8_decode($row['deadline']);
		    $type = utf8_decode($row['type']);
		    $salary = utf8_decode($row['salary']);
		    $location = utf8_decode($row['location']);
		    $desc = utf8_decode($row['description']);
		    $subject = utf8_decode($row['subject']);
		    $miles = ($row['miles']);
		    $paid = utf8_decode($row['paid']);
		    $url = utf8_decode($row['URL']);

	    	
	    	$stmt = mysqli_prepare($conn, "INSERT INTO placementTable (PlacementID, PlacementName, Company, Deadline, Type, Salary, Location, Description, Subject, Miles, Paid, URL) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE PlacementID=VALUES(PlacementID), PlacementName=VALUES(PlacementName), Company=VALUES(Company), Deadline=VALUES(Deadline), Type=VALUES(Type), Salary=VALUES(Salary), Location=VALUES(Location), Description=VALUES(Description), Subject=VALUES(Subject), Miles=VALUES(Miles), Paid=VALUES(Paid), URL=VALUES(URL)");
	   	 	mysqli_stmt_bind_param($stmt, 'issssssssiss', $ID, $name, $company, $deadline, $type, $salary, $location, $desc, $subject, $miles, $paid, $url);
			mysqli_stmt_execute($stmt);
		}
		$response['placementData'] = "success_new";

	} else {
		foreach ($jsonArray as $row) {
			$ID = utf8_decode($row['placementID']);
		    $name = utf8_decode($row['placementName']);
		    $company = utf8_decode($row['company']);
		    $deadline = utf8_decode($row['deadline']);
		    $type = utf8_decode($row['type']);
		    $salary = utf8_decode($row['salary']);
		    $location = utf8_decode($row['location']);
		    $desc = utf8_decode($row['description']);
		    $subject = utf8_decode($row['subject']);
		    $miles = utf8_decode($row['miles']);
		    $paid = utf8_decode($row['paid']);
		    $url = utf8_decode($row['URL']);

	    	
	    	$stmt = mysqli_prepare($conn, "INSERT INTO placementTable (PlacementID, PlacementName, Company, Deadline, Type, Salary, Location, Description, Subject, Miles, Paid, URL) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
	   	 	mysqli_stmt_bind_param($stmt, 'issssssssiss', $ID, $name, $company, $deadline, $type, $salary, $location, $desc, $subject, $miles, $paid, $url);
			mysqli_stmt_execute($stmt);
			$response['placementData'] = "success";
		}
	}



	$profile_Array = $_POST['user_array'];
	$jsonArray2 = json_decode($profile_Array, true);

	foreach ($jsonArray2 as $row2) {
		$userID = utf8_decode($row2['userID']);
	    $username = utf8_decode($row2['username']);
	    $person_name = utf8_decode($row2['name'] ?? null);
	    $email = utf8_decode($row2['email'] ?? null);
	    $phone = utf8_decode($row2['number'] ?? null);
	    $dob = utf8_decode($row2['DOB'] ?? null);
	    $address = utf8_decode($row2['address'] ?? null);
	    $about = utf8_decode($row2['about'] ?? null);
	    $experience = utf8_decode($row2['experience'] ?? null);
	    $uni = utf8_decode($row2['university'] ?? null);

    	
    	$stmt = mysqli_prepare($conn, "INSERT INTO profileTable (UserID, Username, Name, Email, Phone, DOB, Address, About, Experience, University) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE Username='$username', Name='$person_name', Email='$email', Phone='$phone', DOB='$dob', Address='$address', About='$about', Experience='$experience', University='$uni'");
   	 	mysqli_stmt_bind_param($stmt, 'isssisssss', $userID, $username, $person_name, $email, $phone, $dob, $address, $about, $experience,$uni);
		mysqli_stmt_execute($stmt);
		$response['profileData'] = "success";
}

	$pref_Array = $_POST['pref_array'];
	$jsonArray3 = json_decode($pref_Array, true);

	foreach ($jsonArray3 as $row3) {
		$prefID = utf8_decode($row3['prefID']);
		$userID = utf8_decode($row3['userID']);
	    $paid = utf8_decode($row3['paid'] ?? null);
	    $type = utf8_decode($row3['type'] ?? null);
	    $subject = utf8_decode($row3['subject'] ?? null);
	    $miles = utf8_decode($row3['miles'] ?? null);
	    $relocate = utf8_decode($row3['relocate'] ?? null);

    	$stmt = mysqli_prepare($conn, "INSERT INTO preferencesTable (PrefID, UserID, Paid, Type, Subject, Miles, Relocate) VALUES (?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE PrefID='$prefID', UserID='$userID', Paid='$paid', Type='$type', Subject='$subject', Miles='$miles', Relocate='$relocate'");
   	 	mysqli_stmt_bind_param($stmt, 'iisssii', $prefID, $userID, $paid, $type, $subject, $miles, $relocate);
		mysqli_stmt_execute($stmt);
		$response['preferenceData'] = "success";
	}

	$fav_Array = $_POST['favourite_array'];
	$jsonArray4 = json_decode($fav_Array, true);

	foreach ($jsonArray4 as $row4) {
		$favID = utf8_decode($row4['FavID']);
		$userID = utf8_decode($row4['UserID']);
	    $placementID = utf8_decode($row4['PlacementID']);

    	$stmt = mysqli_prepare($conn, "INSERT INTO userFavTable (FavID, UserID, PlacementID) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE FavID='$favID', UserID='$userID', PlacementID='$placementID'");
   	 	mysqli_stmt_bind_param($stmt, 'iii', $favID, $userID, $placementID);
		mysqli_stmt_execute($stmt);
		$response['favData'] = "success";
	}

} else {
	$response['error'] = true;
	$response['message'] = "Invalid request";
}
//displaying the data in json format
echo json_encode($response);