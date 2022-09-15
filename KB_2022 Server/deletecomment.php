<?php
error_reporting(E_ALL);
ini_set('display_error', 1);

include('dbcon.php');

$cno = isset($_POST['cno']) ? $_POST['cno'] : "";
$cpw = isset($_POST['cpw']) ? $_POST['cpw'] : "";

$response = array();
$response["success"] = false;

if($cpw != ""){
    $sql = "SELECT * FROM comment where cno = '$cno' AND cpw = '$cpw';";
    $stmt = $con->prepare($sql);
    $stmt->execute();

    if($stmt->rowCount() == 0)
    {
        $response["success"] = false;
    }
    else{
        $sql = "DELETE FROM comment where cno = '$cno';";
        $stmt = $con->prepare($sql);
        $stmt->execute();
        
        $m_sql = "SET @CNT = 0;";
        $stmt = $con->prepare($m_sql);
        $stmt->execute();
        
        $m_sql = "UPDATE comment SET comment.cno = @CNT := @CNT+1;";
        $stmt = $con->prepare($m_sql);
        $stmt->execute();

        $m_sql = "ALTER TABLE comment AUTO_INCREMENT = 1;";
        $stmt = $con->prepare($m_sql);
        $stmt->execute();

        $response["success"] = true;

    }
}
else{
    $response["success"] = false;
}

header('Content-Type: application/json; charset=utf8');
echo json_encode($response, JSON_PRETTY_PRINT+JSON_UNESCAPED_UNICODE);


?>