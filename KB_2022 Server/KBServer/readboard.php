<?php

error_reporting(E_ALL);
ini_set('display_error', 1);

include('dbcon.php');

$stmt = $con->prepare('SELECT bno, title, name, islike FROM board order by bno desc');
$stmt->execute();

if($stmt->rowCount() > 0)
{
    $data = array();

    while($row=$stmt->fetch(PDO::FETCH_ASSOC))
    {
        extract($row);

        array_push($data,
            array('bno'=>$bno,
            'title'=>$title,
            'name'=>$name,
            'islike'=>$islike
    ));
    }
}
header('Content-Type: application/json; charset=utf8');
$json = json_encode(array("board_list"=>$data), JSON_PRETTY_PRINT+JSON_UNESCAPED_UNICODE);
echo $json;




/*mysqli_query($con, 'SET NAMES utf8');
$statement = mysqli_prepare($con, "SELECT bno, title, name, content, islike FROM board order by bno desc");
mysqli_stmt_execute($statement);

mysqli_stmt_store_result($statement);
mysqli_stmt_bind_result($statement, $boardNo, $title, $userName, $content, $islike);

$response = array();
$response["success"] = false;

while(mysqli_stmt_fetch($statement)) {
    $response["success"] = true;
    $response["boardNo"] = $boardNo;
    $response["title"] = $title;
    $response["userName"] = $userName;
    $response["content"] = $content;
    $response["islike"] = $islike;
}*/




?>
