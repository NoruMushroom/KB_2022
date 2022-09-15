<?php

$con = mysqli_connect("123.215.162.92", "kbuser", "1234", "kb");
mysqli_query($con, 'SET NAMES utf8');

$userID = $_POST["userID"];
$userPassword = $_POST["userPassword"];

$statement = mysqli_prepare($con, "SELECT * FROM user WHERE userID = ? AND password = ?");
mysqli_stmt_bind_param($statement, "ss", $userID, $userPassword);
mysqli_stmt_execute($statement);

mysqli_stmt_store_result($statement);
mysqli_stmt_bind_result($statement, $userID, $userPassword, $userName, $userGender);

$response = array();
$response["success"] = false;

while(mysqli_stmt_fetch($statement)) {
    $response["success"] = true;
    $response["userID"] = $userID;
    $response["userPassword"] = $userPassword;
    $response["userName"] = $userName;
    $response["userGender"] = $userGender;
}

echo json_encode($response);
?>