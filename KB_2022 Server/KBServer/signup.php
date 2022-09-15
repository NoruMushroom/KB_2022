<?php
error_reporting(E_ALL);
ini_set('display_error', 1);

include('dbcon.php');

$userID = isset($_POST['userID']) ? $_POST['userID'] : "";
$userPassword = isset($_POST['userPassword']) ? $_POST['userPassword'] : "";
$userName = isset($_POST['userName']) ? $_POST['userName'] : "";
$userGender = isset($_POST['userGender']) ? $_POST['userGender'] : "";

$response["success"] = false;
$stmt = $con->prepare("SELECT * FROM user WHERE userid = '$userID';");
$stmt->execute();

if($stmt->rowCount() == 0){
    $stmt = $con->prepare("INSERT INTO user VALUES('$userID', '$userPassword', '$userName', '$userGender');");
    $stmt->execute();
    $response["success"] = true;
}

header('Content-Type: application/json; charset=utf8');
$json = json_encode($response, JSON_PRETTY_PRINT+JSON_UNESCAPED_UNICODE);
echo $json;

?>