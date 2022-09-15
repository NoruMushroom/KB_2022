<?php
error_reporting(E_ALL);
ini_set('display_error', 1);

include('dbcon.php');

$userID = isset($_POST['userID']) ? $_POST['userID'] : "";
$mon = isset($_POST['mon']) ? $_POST['mon'] : "";

//$userID = '123';
//$mon = '7';

$sql = "SELECT * FROM ".$mon."mon WHERE userid = '$userID'";
$stmt = $con->prepare($sql);
$stmt->execute();

$weight = array();
$weight["response"] = false;
if($stmt->rowCount() != 0){
    $weight = $stmt->fetch(PDO::FETCH_ASSOC);
    $weight["response"] = true;
}

header('Content-Type: application/json; charset=utf8');
$json = json_encode($weight, JSON_PRETTY_PRINT+JSON_UNESCAPED_UNICODE);
echo $json;

?>