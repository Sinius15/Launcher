<?php
function query ($query){
    $con=mysqli_connect("db.sinius15.com","md311886db272678",file_get_contents("hidden/pass.txt"),"md311886db272678");
    if (mysqli_connect_errno($con)){
        echo "Failed to connect to MySQL: " . mysqli_connect_error();
        return;
    }
    $result = mysqli_query($con,$query);
    $row = mysqli_fetch_array($result);
    mysqli_close($con);
    return $row['out'];
}

$req = $_GET['req'];
$game = $_GET['game'];

if($req == 'versionList'){
    if($game == 'suite')
        $myDirectory = opendir("./suite");
    if($game == 'chess')
        $myDirectory = opendir("./chess");
    if($game == 'maze')
        $myDirectory = opendir("./maze");
    
    while($entryName=readdir($myDirectory)) {
        $dirArray[]=($entryName);
    }
    $indexCount=count($dirArray);
    closedir($myDirectory);
    $outputText = "";
    for($index=0; $index < $indexCount; $index++) {
        $name=$dirArray[$index];
        if(substr_compare($name, ".zip", -4, 4) === 0 && $name !== "" && $name !== ".")
            if($outputText === "")
                $outputText = str_replace(".zip", "", $name, $count);
            else
                $outputText = $outputText . "," . str_replace(".zip", "", $name, $count);
    }
    echo("$outputText");
}
else if($req == 'latestVersion'){
    if($game == 'suite')
        $myDirectory = opendir("./suite");
    if($game == 'chess')
        $myDirectory = opendir("./chess");
    if($game == 'maze')
        $myDirectory = opendir("./maze");
    
    while($entryName=readdir($myDirectory)) {
        $dirArray[]=($entryName);
    }
    $indexCount=count($dirArray);
    closedir($myDirectory);
    $outputText = "";
    for($index=0; $index < $indexCount; $index++) {
        $name=$dirArray[$index];
        if(substr_compare($name, ".zip", -4, 4) === 0 && $name !== "" && $name !== ".")
            $outputText = str_replace(".zip", "", $name, $count);
    }
    echo("$outputText");
}
else if($req == 'login'){
        $builder = "
            SELECT CASE WHEN EXISTS (
            SELECT * 
            FROM Players
            WHERE name =  '". $_GET['name'] ."'
            AND pass =  '". CRYPT($_GET['pass'], file_get_contents('hidden/hash.txt')) ."'
            )
            THEN 1 
            ELSE 0 
            END
            AS 'out'
        ";
        $out = query($builder);
        if($out === '0')
            echo('false');
        if($out === '1')
            echo('true');
}
else if($req === 'existsPlayer'){
    $builder = "
            SELECT CASE WHEN EXISTS (
            SELECT * 
            FROM Players
            WHERE name =  '". $_GET['name'] ."'
            )
            THEN 1 
            ELSE 0 
            END
            AS 'out'
        ";
    $out = query($builder);
    if($out === '0')
        echo('false');
    if($out === '1')
        echo('true');
}
else if($req === 'existsEmail'){
    $builder = "
            SELECT CASE WHEN EXISTS (
            SELECT * 
            FROM Players
            WHERE mail =  '". $_GET['mail'] ."'
            )
            THEN 1 
            ELSE 0 
            END
            AS 'out'
        ";
    $out = query($builder);
    if($out === '0')
        echo('false');
    if($out === '1')
        echo('true');
}

?>