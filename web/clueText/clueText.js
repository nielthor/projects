const controlsDiv = document.getElementById("controls");
const geussDiv = document.getElementById("guess");
const resultDiv = document.getElementById("result");
const rControlDiv = document.getElementById("recordControl");
const recordDiv = document.getElementById("records");
const enterBtn = document.getElementById("enter");
const guessBtn = document.getElementById("guessButton");

const masterRoom =["Kitchen", "Ball Room", 
                    "Conservatory", "Dining Room", 
                    "Billiard Room", "Lounge", 
                    "Hall", "Library", "Study"];
const masterSuspect =["Col Mustard", "Miss Scarlet", 
                        "Mr Green", "Mrs Peacock", 
                        "Mrs White", "Prof Plum"];
const masterWeapon =["Candlestick", "Knife", 
                        "Lead Pipe", "Revolver", 
                        "Rope", "Wrench"];
var weaponsArray = [...masterWeapon];
var roomArray = [...masterRoom];
var suspectArray = [...masterSuspect];
var computerCards = {
    room: [],
    suspect: [],
    weapon: [],
}
var computerChoice = {
    room: [...masterRoom],
    suspect: [...masterSuspect],
    weapon: [...masterWeapon],
}
var playerChoice = {
    room: [],
    suspect: [],
    weapon: [],
}
var secretTuple = {
    room: "",
    suspect:"",
    weapon: "",
}
var playerGuess = {
    player: "", 
    room: "",
    suspect:"",
    weapon: "",
}
var player = {
    name:"",
    date:"",
    outcome:"",
}
var date = new Date();

//active on screen
enterBtn.addEventListener("click", init);
guessBtn.addEventListener("click", makeGuess);


function init(){
    if(document.getElementById("userinput").value == ""){
        alert("Player name required");
        return;
    }
    //set palyer
    player.name = document.getElementById("userinput").value;
    player.date = date.toDateString();
    //remove lables, input and button
    controlsDiv.removeChild(document.getElementById("userlabel"));
    controlsDiv.removeChild(document.getElementById("userinput"));
    controlsDiv.removeChild(document.getElementById("enter"));
    //create node and add to controls
    var p = document.createElement("p");
    p.setAttribute("id", "usermessage");
    p.innerHTML = "Hello " + player.name + ", you hold the cards for ";
    controlsDiv.append(p);
    setTuple();
    divideCards(roomArray, playerChoice.room, computerCards.room);
    divideCards(suspectArray, playerChoice.suspect, computerCards.suspect);
    divideCards(weaponsArray, computerCards.weapon, playerChoice.weapon);

    setTimeout(()=>{
        //add the cards to title bar
        p.innerHTML += playerChoice.room.toString().replace(/,/g, ", ") + ", " + 
        playerChoice.suspect.toString().replace(/,/g, ", ") + ", " + 
        playerChoice.weapon.toString().replace(/,/g, ", ");
        //load computer choices
        updateComputerChoice(computerCards.room);
        updateComputerChoice(computerCards.suspect);
        updateComputerChoice(computerCards.weapon);
        //checking my cards
        console.log(playerChoice);
        console.log(computerChoice);
        console.log(secretTuple);

        updateSelectBox(playerChoice.room, "roomoption");
        updateSelectBox(playerChoice.weapon, "weaponoption");
        updateSelectBox(playerChoice.suspect, "suspectoption");

        guessBtn.disabled = false;

        let historyBtn = document.createElement("button");
        historyBtn.setAttribute("id", "historyButton");
        historyBtn.innerHTML = "Show History";
        historyBtn.addEventListener("click", viewHitory);
        rControlDiv.append(historyBtn);
        let recordBtn = document.createElement("button");
        recordBtn.setAttribute("id", "recordButton");
        recordBtn.innerHTML = "Show Records";
        recordBtn.addEventListener("click", viewRecords);
        rControlDiv.append(recordBtn);

    }, 0);
}

//get cards
function getRandomCard(aArray){
    return aArray.splice(Math.floor(Math.random()*aArray.length), 1);
}
//get random secret cards
async function  setTuple(){
    let wCard = getRandomCard(weaponsArray.splice(Math.floor(Math.random()*weaponsArray.length), 1));
    let sCard = getRandomCard(suspectArray.splice(Math.floor(Math.random()*suspectArray.length), 1));
    let rCard = getRandomCard(roomArray.splice(Math.floor(Math.random()*roomArray.length), 1));
    let values = await Promise.all([wCard, sCard, rCard]);
    secretTuple.weapon = values[0].toString();
    secretTuple.suspect = values[1].toString();
    secretTuple.room = values[2].toString();
}

async function divideCards(aArray, play1, play2){
    //shuffle and distrabute
    while(aArray.length > 0){
        let cCard = getRandomCard(aArray);
        let pCard = getRandomCard(aArray);
        let values = await Promise.all([cCard, pCard]);
        if(values[0].toString() != ""){
            play1.push(values[0].toString());
        }
        if(values[1].toString() != ""){
            play2.push(values[1].toString()); 
        }   
    }
}

//remove from select box
function updateSelectBox(aArray, boxName){
    let selectBox = document.getElementById(boxName);
    console.log(selectBox);
    aArray.forEach(card =>{
        selectBox.remove(document.getElementsByName(card)[0].index);
    });
}

//set computers choice cars
function updateComputerChoice(aArray){
    aArray.forEach(card =>{
        if(computerChoice.room.includes(card)){
            computerChoice.room.splice(computerChoice.room.indexOf(card), 1);
        }else if(computerChoice.suspect.includes(card)){
            computerChoice.suspect.splice(computerChoice.suspect.indexOf(card), 1);
        }else if(computerChoice.weapon.includes(card)){
            computerChoice.weapon.splice(computerChoice.weapon.indexOf(card), 1);
        }
    });
}

//players guess
function makeGuess(){
    //get values from select boxes
    playerGuess.player = player.name;
    playerGuess.room = document.getElementById("roomoption").value;
    playerGuess.suspect = document.getElementById("suspectoption").value;
    playerGuess.weapon = document.getElementById("weaponoption").value;

    if(isMatch()){
        //player wins
        player.outcome = "win";            
        //set mesage
        let aMessage = "That was the correct guess! " + 
                        playerGuess.suspect + " did it with the " + 
                        playerGuess.weapon + " in the " + playerGuess.room;
        addMessage(aMessage, "win");
        //store in local storage
        let d = new Date();
        localStorage.setItem(d, JSON.stringify(player));
    }else{
        //get card from computer
        let hasCard = oppentsCard([playerGuess.room, playerGuess.suspect, playerGuess.weapon], computerCards);
        //dispaly message
        let aMessage = "Sorry that was an incorrect guess! The Computer holds the card for " + hasCard;
        addMessage(aMessage, "computer");
        guessBtn.disabled = true;
        //store in session
        let d = new Date();
        sessionStorage.setItem(d, JSON.stringify(playerGuess));
    }
}

//computers move
function computerMove(){
    //set play guess values
    playerGuess.player = "Computer";
    playerGuess.room = computerChoice.room[Math.floor(Math.random()*computerChoice.room.length)];
    playerGuess.suspect = computerChoice.suspect[Math.floor(Math.random()*computerChoice.suspect.length)];
    playerGuess.weapon = computerChoice.weapon[Math.floor(Math.random()*computerChoice.weapon.length)];

    if(isMatch()){
        //player loss
        player.outcome = "Loss";            
        //set mesage
        let aMessage = "The Computer geussed correct! " + 
                        playerGuess.suspect + " did it with the " + 
                        playerGuess.weapon + " in the " + playerGuess.room;
        addMessage(aMessage, "win");
        //store in local storage
        let d = new Date();
        localStorage.setItem(d, JSON.stringify(player));
    }else{
        //get card from player
        let hasCard = oppentsCard([playerGuess.room, playerGuess.suspect, playerGuess.weapon], playerChoice);
        //remove from computers choices
        updateComputerChoice([hasCard]);
        //dispaly message
        let aMessage = "The Computer guesed: " + playerGuess.suspect + 
                        " in the " + playerGuess.room + 
                        " with the " + playerGuess.weapon + "<br>" + 
                        "The Computer made an incorrect guess! You hold the card for " + hasCard;
        addMessage(aMessage, "reset");
        //store in session
        let d = new Date();
        sessionStorage.setItem(d, JSON.stringify(playerGuess));
    }
}

//check if player matches secret tuple
function isMatch(){
    return (secretTuple.room == playerGuess.room && secretTuple.suspect == playerGuess.suspect && secretTuple.weapon == playerGuess.weapon);
}

//gets a guessed card from oppents deck
function oppentsCard(guessArray, opntArray){
    let hasCards = [];
    guessArray.forEach(card => {
        if(opntArray.room.includes(card)){
            hasCards.push(card);
        }else if(opntArray.suspect.includes(card)){
            hasCards.push(card);
        }else if(opntArray.weapon.includes(card)){
            hasCards.push(card);
        }
    });
    return  hasCards[Math.floor(Math.random()*hasCards.length)]; 
}

//add message to results div
function addMessage(message, control){
    let p = document.createElement("p");
    p.innerHTML = message + "<br>";
    let btn = document.createElement("button");
    btn.setAttribute("id", "continueButton");
    btn.innerHTML = "Continue";
    if(control == "win"){
        btn.addEventListener("click", resetGame);
    }else if(control == "reset"){
        btn.addEventListener("click", reset);
    }else{
        btn.addEventListener("click", computerMove);
    }
    
    //remove any nodes
    while(resultDiv.firstChild){
        resultDiv.removeChild(resultDiv.firstChild);
    }
    //add new nodes
    resultDiv.append(p);
    resultDiv.append(btn);  
}

//Start new game -----------posibly remove
function reset(){
    //remove any nodes
    while(resultDiv.firstChild){
        resultDiv.removeChild(resultDiv.firstChild);
    }
    guessBtn.disabled = false;
}

//Show history
function viewHitory(){
    let histBtn = document.getElementById("historyButton");
    removeChild(recordDiv);
    let p = document.createElement("p");
    recordDiv.append(p);
    //add history
    if(histBtn.innerHTML == "Show History"){
        document.getElementById("recordButton").innerHTML = "Show Records";
        histBtn.innerHTML = "Hide History"
        console.log(sessionStorage);
        for(let i = 0; i < sessionStorage.length; i++){
            let aMove = JSON.parse(sessionStorage.getItem(sessionStorage.key(i)));
            p.innerHTML += aMove.player + " guessed: " + aMove.suspect + 
                            " in the " + aMove.room + " with the " + aMove.weapon + "<br>";
        }
    }else{
        removeChild(recordDiv);
        histBtn.innerHTML = "Show History"
    }
}
//show record
function viewRecords(){
    let recBtn = document.getElementById("recordButton");
    removeChild(recordDiv);
    let p = document.createElement("p");
    recordDiv.append(p);
    let computerWin = 0;
    if(recBtn.innerHTML == "Show Records"){
        document.getElementById("historyButton").innerHTML = "Show History";
        recBtn.innerHTML = "Hide Records"     
        for(let i = 0; i < localStorage.length; i++){
            let aPlayer = JSON.parse(localStorage.getItem(localStorage.key(i)));
            p.innerHTML += aPlayer.name + " played on " + aPlayer.date + 
                            " with outcome: " + aPlayer.outcome +  "<br>";
            if(aPlayer.outcome == "Loss"){
                computerWin += 1;
            }
        }
        p.innerHTML = "Computer Won " + computerWin + " of " + localStorage.length + "<br>" + p.innerHTML;
    }else{
        removeChild(recordDiv);
        recBtn.innerHTML = "Show Records"
    }
}

//remove nodes
function removeChild(aDiv){
        //remove any nodes
        while(aDiv.firstChild){
            aDiv.removeChild(aDiv.firstChild);
        }
}

//reset game
function resetGame(){
    sessionStorage.clear();
    removeChild(resultDiv);
    removeChild(recordDiv);
    removeChild(rControlDiv);
    guessBtn.disabled = true;
    //reset arrays and objects
    weaponsArray = [...masterWeapon];
    roomArray = [...masterRoom];
    suspectArray = [...masterSuspect];
    computerCards = {room: [], suspect: [], weapon: [],}
    computerChoice = {room: [...masterRoom], suspect: [...masterSuspect], weapon: [...masterWeapon],}
    secretTuple = { room: "", suspect:"", weapon: "",}
    playerGuess = { player: "",  room: "", suspect:"", weapon: "", }
    player = { name:"", date:"", outcome:"", }

    //reset controls
    controlsDiv.removeChild(document.getElementById("usermessage"));

    let uLbl = document.createElement("label");
    uLbl.setAttribute("id", "userlabel");
    uLbl.setAttribute("for", "userinput");
    uLbl.innerHTML = "Name: ";

    let uInpt = document.createElement("input");
    uInpt.setAttribute("id", "userinput");
    uInpt.setAttribute("placeholder","your name");
    let uBtn = document.createElement("button");
    uBtn.setAttribute("type","submit");
    uBtn.setAttribute("id","enter");
    uBtn.addEventListener("click", init);
    uBtn.innerHTML = "Enter";
    controlsDiv.append(uLbl);
    controlsDiv.append(uInpt);
    controlsDiv.append(uBtn);

    addOptions(masterRoom, playerChoice.room, "roomoption");
    addOptions(masterSuspect, playerChoice.suspect, "suspectoption");
    addOptions(masterWeapon, playerChoice.weapon, "weaponoption");
    playerChoice = {room: [], suspect: [], weapon: [],}
}

function addOptions(mastArray, pArray, boxName){
    let aBox = document.getElementById(boxName);
    mastArray.forEach(opt =>{
        if(pArray.includes(opt)){
            let aOpt = document.createElement("option");
            aOpt.setAttribute("Name", opt);
            aOpt.setAttribute("Value", opt);
            aOpt.innerHTML = opt;
            aBox.add(aOpt);
        }
    });
    
}