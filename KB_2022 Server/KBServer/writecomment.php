<?php
error_reporting(E_ALL);
ini_set('display_error', 1);

include('dbcon.php');

$bno = isset($_POST['bno']) ? $_POST['bno'] : "";
$uname = isset($_POST['uname']) ? $_POST['uname'] :'';
$cpw = isset($_POST['cpw']) ? $_POST['cpw'] :'';
$comment = isset($_POST['comment']) ? $_POST['comment'] :'';

$response = array();
$response["success"] = false;
if($bno != ""){
    $sql = "INSERT INTO comment(bno, name, cpw, comment) VALUES('$bno', '$uname', '$cpw', '$comment');";
    $stmt = $con->prepare($sql);
    $stmt->execute();
    $response["success"] = true;
}

header('Content-Type: application/json; charset=utf8');
echo json_encode($response, JSON_PRETTY_PRINT+JSON_UNESCAPED_UNICODE);
?>

?>