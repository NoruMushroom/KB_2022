<?php
error_reporting(E_ALL);
ini_set('display_error', 1);

include('dbcon.php');

//$userID = isset($_POST['userID']) ? $_POST['userID'] : "";
//$mon = isset($_POST['mon']) ? $_POST['mon'] : "";

$userID = 'KBSC';
$mon = '9';


$sql = "SELECT * FROM user WHERE userid = '$userID'";
$stmt = $con->prepare($sql);
$stmt->execute();

$weight = array();
$weight["response"] = false;

if($stmt->rowCount() != 0){
    $weight["response"] = true;
    for($i=0;$i<=2;$i++){
        $mon_count = (int)$mon - $i;
        $select_mon = (string)$mon_count;
        $sql = "SELECT * FROM ".$select_mon."mon WHERE userid = '$userID'";
        $stmt = $con->prepare($sql);
        $stmt->execute();
        $select_weight = $stmt->fetch(PDO::FETCH_ASSOC);
        $weight["$select_mon"] = $select_weight;
    }
}

header('Content-Type: application/json; charset=utf8');
$json = json_encode($weight, JSON_PRETTY_PRINT+JSON_UNESCAPED_UNICODE);
echo $json;
?>