<?php
error_reporting(E_ALL);
ini_set('display_error', 1);

$server_dir = 'D:/VS Code/Server/Apache24/htdocs/android/user_img/';
$error = $_FILES['myfile']['error'];
$name = $_FILES['myfile']['name'];
$user_dir = explode('.', $name);
$upload_dir = $server_dir.$user_dir[0].'/';
$allowed_ext = array('jpg', 'jpeg', 'png', 'gif');

$response = array();
$response["success"] = false;

$ext = array_pop(explode('.', $name));

if( $error != UPLOAD_ERR_OK ) {
	switch( $error ) {
		case UPLOAD_ERR_INI_SIZE:
		case UPLOAD_ERR_FORM_SIZE:
			$response["error"] = $error;
			break;
		case UPLOAD_ERR_NO_FILE:
			$response["error"] = $error;
			break;
		default:
			$response["error"] = $error;
	}
	exit;
}

if( !in_array($ext, $allowed_ext) ) {
	exit;
}
if(!is_dir($upload_dir)){
	mkdir($upload_dir, 0777, true);
}
move_uploaded_file( $_FILES['myfile']['tmp_name'], "$upload_dir/$name");
$response["success"] = true;

header('Content-Type: application/json; charset=utf8');
echo json_encode($response, JSON_PRETTY_PRINT+JSON_UNESCAPED_UNICODE);
?>