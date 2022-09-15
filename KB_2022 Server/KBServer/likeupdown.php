<?php
error_reporting(E_ALL);
ini_set('display_error', 1);

include('dbcon.php');

$bno = isset($_POST['bno']) ? $_POST['bno'] :'';
$islike = isset($_POST['islike']) ? $_POST['islike'] :'';



$response = array();
$response["success"] = false;


if($islike != ""){
    if($islike == "true"){
        $sql = "UPDATE board SET islike = islike + 1 WHERE bno = '$bno';";
        $stmt = $con->prepare($sql);
        $stmt->execute();

    }
    else{
        $sql = "UPDATE board SET islike = islike - 1 WHERE bno = '$bno';";
        $stmt = $con->prepare($sql);
        $stmt->execute();

    }
    $response["success"] = true;
}
else{

}

header('Content-Type: application/json; charset=utf8');
echo json_encode($response, JSON_PRETTY_PRINT+JSON_UNESCAPED_UNICODE);
?>