<?php
error_reporting(E_ALL);
ini_set('display_error', 1);

include('dbcon.php');

$bno = isset($_POST['bno']) ? $_POST['bno'] : "";
$bpw = isset($_POST['bpw']) ? $_POST['bpw'] : "";


$response = array();
$response["success"] = false;

if($bpw != ""){
    $sql = "SELECT * FROM board where bno = '$bno' AND bpw = '$bpw';";
    $stmt = $con->prepare($sql);
    $stmt->execute();

    if($stmt->rowCount() == 0)
    {
        $response["success"] = false;
    }
    else{
        $sql = "DELETE FROM board where bno = '$bno';";
        $stmt = $con->prepare($sql);
        $stmt->execute();
        
        $t_sql = "SET @CNT = 0;";
        $stmt = $con->prepare($t_sql);
        $stmt->execute();
        
        $t_sql = "UPDATE board SET board.bno = @CNT := @CNT+1;";
        $stmt = $con->prepare($t_sql);
        $stmt->execute();

        $t_sql = "ALTER TABLE board AUTO_INCREMENT = 1;";
        $stmt = $con->prepare($t_sql);
        $stmt->execute();

        $c_sql = "DELETE FROM comment WHERE bno = '$bno';";
        $stmt = $con->prepare($c_sql);
        $stmt->execute();
        
        $c_sql = "SET @CNT = 0;";
        $stmt = $con->prepare($c_sql);
        $stmt->execute();

        $c_sql = "UPDATE comment SET comment.cno = @CNT := @CNT+1;";
        $stmt = $con->prepare($c_sql);
        $stmt->execute();

        $c_sql = "ALTER TABLE comment AUTO_INCREMENT = 1;";
        $stmt = $con->prepare($c_sql);
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