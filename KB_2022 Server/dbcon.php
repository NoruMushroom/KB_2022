<?php

$host = '123.215.162.92';
$dbusername = 'kbuser';
$dbpassword = '1234';
$dbname = 'kb';

$options = array(PDO::MYSQL_ATTR_INIT_COMMAND => 'SET NAMES utf8');

try{
    $con = new PDO("mysql:host={$host};dbname={$dbname};charset=utf8", $dbusername, $dbpassword);
} catch(PDOException $e) {
    die("Failed to connect to the database: ".$e->getMessage());
}

$con->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
$con->setAttribute(PDO::ATTR_DEFAULT_FETCH_MODE, PDO::FETCH_ASSOC);

if(function_exists('get_magic_quotes_gpc') && get_magic_quotes_gpc()) {
    function undo_magice_quotes_gpc(&$array){
        foreach($array as &$value) {
            if(is_array($value)) {
                undo_magice_quotes_gpc($value);
            }
            else{
                $value = stripslashes($value);
            }
        }
    }

    undo_magice_quotes_gpc($_POST);
    undo_magice_quotes_gpc($_GET);
    undo_magice_quotes_gpc($_COOKIE);

    header('Content-Type: text/html; charset=utf-9');
}