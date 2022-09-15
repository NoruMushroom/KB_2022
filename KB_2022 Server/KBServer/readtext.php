<?php

error_reporting(E_ALL);
ini_set('display_error', 1);

include('dbcon.php');

$bno = isset($_POST['bno']) ? $_POST['bno'] : "";
#$bno = "1";
if($bno != ""){
    $sql = "SELECT bno, title, name, content, islike FROM board WHERE bno = $bno";
    $sql2 = "SELECT cno, name, comment FROM comment WHERE bno = $bno ORDER BY cno DESC";
    $stmt = $con->prepare($sql);
    $stmt->execute();
    $stmt2 = $con->prepare($sql2);
    $stmt2->execute();
    
    if($stmt->rowCount() > 0)
    {
        $data = array();
        $data2 = array();
        while($row=$stmt->fetch(PDO::FETCH_ASSOC))
        {
         extract($row);

            array_push($data,
            array('bno'=>$bno,
            'title'=>$title,
            'name'=>$name,
            'content'=>$content,
            'islike'=>$islike
            ));
        }
        while($row=$stmt2->fetch(PDO::FETCH_ASSOC))
        {
            extract($row);
            array_push($data2,
            array('cno'=>$cno,
            'name'=>$name,
            'comment'=>$comment
            ));
        }
    }
}

header('Content-Type: application/json; charset=utf8');
$json = json_encode(array("select_content"=>$data,"comment_list"=>$data2), JSON_PRETTY_PRINT+JSON_UNESCAPED_UNICODE);
echo $json;
?>