<?php
error_reporting(E_ALL);
ini_set('display_error', 1);

include('dbcon.php');

$userID = isset($_POST['userID']) ? $_POST['userID'] : "";
$userPW = isset($_POST['userPW']) ? $_POST['userPW'] : "";
$changePW = isset($_POST['changePW']) ? $_POST['changePW'] : "";

$response = array();
$response["success"] = false;

$sql = "SELECT * FROM user where userid = '$userID' AND password = '$userPW';";
$stmt = $con->prepare($sql);
$stmt->execute();

if($stmt->rowCount() > 0)
{
    $sql = "UPDATE user SET password = '$changePW' WHERE userid = '$userID';";
    $stmt = $con->prepare($sql);
    $stmt->execute();
    $response["success"] = true;
}
header('Content-Type: application/json; charset=utf8');
echo json_encode($response, JSON_PRETTY_PRINT+JSON_UNESCAPED_UNICODE);

?>