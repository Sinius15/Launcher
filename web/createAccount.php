<?php
$name = $_POST["name"];
$pass = $_POST["pass"];
$pass_cryp = CRYPT($pass, file_get_contents('hidden/hash.txt'));
$mail = $_POST["mail"];

$databasePass = (string)file_get_contents("hidden/pass.txt");

function query ($query){
    $con=mysqli_connect('db.sinius15.com','md311886db272678',file_get_contents("hidden/pass.txt") ,'md311886db272678');
    if (mysqli_connect_errno($con)){
        echo "Failed to connect to MySQL: " . mysqli_connect_error();
        return;
    }
    $result = mysqli_query($con,$query);
    $row = mysqli_fetch_array($result);
    mysqli_close($con);
    return $row['out'];
}

function existsPlayer($n){
    $builder = "
            SELECT CASE WHEN EXISTS (
            SELECT * 
            FROM Players
            WHERE name =  '". $n ."'
            )
            THEN 1 
            ELSE 0 
            END
            AS 'out'
        ";
    $out = query($builder);
    if($out === '0')
        return false;
    if($out === '1')
        return true;
}
function existsMail($n){
    $builder = "
            SELECT CASE WHEN EXISTS (
            SELECT * 
            FROM Players
            WHERE mail =  '". $n ."'
            )
            THEN 1 
            ELSE 0 
            END
            AS 'out'
        ";
    $out = query($builder);
    if($out === '0')
        return false;
    if($out === '1')
        return true;
}

if($name === "" or strpos($name, " ") or strpos($name, "?") or strpos($name, "=")){
    echo("the name field is not filled in correctly");
    return;
}
if($pass === "" or strpos($pass, " ")){
    echo("the password field is not filled in correctly");
    return;
}
if($mail === "" or strpos($mail, " ")){
    echo("the e-mail field is not filled in correctly");
    return;
}
if(existsPlayer($name)){
    echo("The username is already taken. please pick an other one.");
    return;
}
if(existsMail($mail)){
    echo("The email address is already been used. please pick an other one.");
    return;
}
$builder = "
INSERT INTO  `md311886db272678`.`Players` (
`ID` ,
`name` ,
`mail` ,
`pass`
)
VALUES (
NULL ,  '". $name ."',  '". $mail ."',  '". $pass_cryp ."'
);
";
$con=mysqli_connect("db.sinius15.com","md311886db272678",$databasePass,"md311886db272678");
if (mysqli_connect_errno($con)){
    echo ("Failed to connect to MySQL: " . mysqli_connect_error() . "<br>");
    return;
}
mysqli_query($con, $builder);
mysqli_close($con);
echo("done!");

?>