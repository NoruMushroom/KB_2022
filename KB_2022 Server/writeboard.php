<?php
error_reporting(E_ALL);
ini_set('display_error', 1);

include('dbcon.php');

$title = isset($_POST['title']) ? $_POST['title'] :'';
$uname = isset($_POST['uname']) ? $_POST['uname'] :'';
$upw = isset($_POST['upw']) ? $_POST['upw'] :'';
$content = isset($_POST['content']) ? $_POST['content'] :'';
$response = array();
$response["success"] = false;
if($title != ""){
    $sql = "INSERT INTO board(title, name, bpw, content) VALUES('$title', '$uname', '$upw', '$content');";
    $stmt = $con->prepare($sql);
    $stmt->execute();
    $response["success"] = true;
}

header('Content-Type: application/json; charset=utf8');
echo json_encode($response, JSON_PRETTY_PRINT+JSON_UNESCAPED_UNICODE);
?>