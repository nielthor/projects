const rooms = document.querySelectorAll('.room');
const cards = document.querySelectorAll('.card');
const rows = document.querySelectorAll('.row');
const clearBtn = document.getElementById('clear');
const submitBtn = document.getElementById('submit');
const controlsDiv = document.getElementById('controls');
const guessDiv = document.getElementById('guess');
const historyDiv = document.getElementById('history');
const recordsDiv = document.getElementById('records');

const masterRoom = ["Kitchen","Ball Room","Conservatory", "Dining Room","Billiard Room","Lounge", "Hall","Library","Study"];
const masterweapon =["Candlestick", "Knife", "Lead Pipe", "Revolver", "Rope","Wrench"];
const masterSuspect =["Col Mustard","Miss Scarlet" ,"Mr Green","Mrs Peacock","Mrs White","Prof Plum"];

var roomArray = [...masterRoom]; 
var weaponsArray = [...masterweapon];
var suspectArray = [...masterSuspect];

var gameArray=[];
var ComputerArray= [];
var computerGuessArray={
    room: [...roomArray],
    weapon: [...weaponsArray],
    suspect:[...suspectArray],
};
var playerArray=[];
var secretTuple= {room: "", weapon: "", suspect:""}
var date = new Date();
var guessCount = 0;
var gameOver = false;

var player = {
    name: "",
    date: "",
    outcome: "",
}
var playerMove = {
    weapon: {name: "", location: ""},
    suspect: {name: "", location: ""}
}
//add event listeners
var moveHistory = new Map();
var draggedItem = null;
for(const card of cards){
    card.addEventListener('dragstart', dragStart);
    card.addEventListener('dragend', dragEnd);
}
for(const room of rooms){
    room.addEventListener('dragover', dragOver);
    room.addEventListener('dragenter', dragEnter);
    room.addEventListener('drop', dragDrop);  
}
for(const row of rows){
    row.addEventListener('dragover', dragOver);
    row.addEventListener('dragenter', dragEnter);
    row.addEventListener('drop', dragDrop);  
}
submitBtn.addEventListener('click', init);


//drag drop functions
function dragStart(e){
    //console.log(e.target);
    draggedItem = e.target;
    setTimeout(()=>{draggedItem.style.display = 'none';},0);
    if(!moveHistory.get(e.target.id)){
        moveHistory.set(draggedItem.id, e.target.parentNode.id);
    }
}
function dragEnd(e){
    //console.log(draggedItem);
    setTimeout(()=>{
                draggedItem.style = 'row';
                    //draggedItem = null;
                }, 0);
}
function dragOver(e){
    if(e.target.className == 'row' || e.target.className == 'room'){
        e.preventDefault();
    }
    
}
function dragEnter(e){
    if(e.target.className == 'row'  || e.target.className == 'room'){
        e.preventDefault();
    }
}
function dragDrop(e){
    e.target.append(draggedItem);
    //console.log(moveHistory);
    if(e.target.className == 'row'){
        document.getElementById(moveHistory.get(draggedItem.id)).append(draggedItem); 
        moveHistory.delete(draggedItem.id);
    }
    //console.log(moveHistory);
    if(moveHistory.get(draggedItem.id) == "suspectRow"){
    playerMove.suspect.location = e.target.id;
    playerMove.suspect.name = draggedItem.id;
    }else{
        playerMove.weapon.location = e.target.id;
        playerMove.weapon.name = draggedItem.id;
    }

}

//game init
function init(){
    sessionStorage.clear();
    gameOver = false;
    if(document.getElementById('userInput').value =="" ||document.getElementById('userInput').value == undefined){
        alert("Player Name Required");
        return;
    }
    //set secret tuple
    setTuple();
    //divide cards between play and computer
    divideCards()

    //get player
    player.name = document.getElementById('userInput').value;
    player.date = date.toDateString();
    //remove input
    controlsDiv.removeChild(document.getElementById('userInput'));
    //remove button

    //add label with welcome <name>
    let lbl = document.createElement("label");
    let node = document.createTextNode("Welcome " + player.name);
    lbl.append(node);
    controlsDiv.insertBefore(lbl, controlsDiv.childNodes[0]);
    //change submit to not you
    submitBtn.innerHTML = "Not You?";
    submitBtn.removeEventListener('click', init);
    submitBtn.addEventListener('click', resetGame);

setTimeout(()=>{
    console.log(secretTuple);
    //console.log(playerArray);
    //console.log(ComputerArray);
    //console.log(player);
    computerChoices(ComputerArray);
    //console.log(computerGuessArray);

    //add list of your cards
    let playerStr =  playerArray.toString();
    let playerFrmt = playerStr.replace(",", ", ")
    let par = document.createElement("p");
    par.setAttribute("id", "playerCards");
    let pNode = document.createTextNode("Your cards: " + playerArray.toString().replace(/,/g,  ", "));
    par.append(pNode);
    controlsDiv.append(par);

    //add submit guess
    let guesssBtn = document.createElement("button");
    guesssBtn.setAttribute("id", "guessButton");
    guesssBtn.innerHTML = "Submit Guess";
    controlsDiv.append(guesssBtn);
    guesssBtn.addEventListener('click', makeGuess);

    //add continue and reset board (same line)
    let continueBtn = document.createElement("button");
    continueBtn.setAttribute("id", "continueButton");
    continueBtn.innerHTML = "Continue";
    continueBtn.addEventListener("click", computerGuess);
    continueBtn.disabled = true;
    guessDiv.append(continueBtn);

    let resetBtn = document.createElement("button");
    resetBtn.setAttribute("id", "resetButton");
    resetBtn.addEventListener("click", resetBoard);
    resetBtn.innerHTML = "Reset";
    guessDiv.append(resetBtn);

    //show history
    let historyBtn = document.createElement("button");
    historyBtn.setAttribute("id", "historyButton");
    historyBtn.innerHTML = "Show History";
    historyBtn.setAttribute("value", "show");
    historyBtn.addEventListener("click", viewMoves);
    historyDiv.append(historyBtn);
    //show record
    let recordBtn = document.createElement("button");
    recordBtn.setAttribute("id", "recordButton");
    recordBtn.innerHTML = "Show Record";
    recordBtn.setAttribute("value", "show");
    recordBtn.addEventListener("click", viewRecord);
    recordsDiv.append(recordBtn);
}, 0);


    //add submit guess button

}

//reset game
function resetGame(){
    resetBoard();
    playerArray = [];
    ComputerArray = [];
    guessCount = 0;

    player = {
        name: "",
        date: "",
        outcome: "",
    };
 
    playerMove = {
        weapon: {name: "", location: ""},
        suspect: {name: "", location: ""}
    };

    secretTuple= {room: "", weapon: "", suspect:""};

    roomArray = [...masterRoom]; 
    weaponsArray = [...masterweapon];
    suspectArray = [...masterSuspect];
    computerGuessArray={
        room: [...masterRoom],
        weapon: [...masterweapon],
        suspect:[...masterSuspect],
    };

    submitBtn.innerHTML = "Submit";
    submitBtn.addEventListener('click', init);
    submitBtn.removeEventListener('click', resetGame);

    //remove elements
    controlsDiv.removeChild(controlsDiv.childNodes[0]);
    controlsDiv.removeChild(document.getElementById('playerCards'));
    controlsDiv.removeChild(document.getElementById('guessButton'))
    if(controlsDiv.contains(document.getElementById('moveDescr'))){
        controlsDiv.removeChild(document.getElementById('moveDescr'));
    }
    guessDiv.removeChild(document.getElementById('continueButton'));
    guessDiv.removeChild(document.getElementById('resetButton'));
    historyDiv.removeChild(document.getElementById('historyButton'));
    recordsDiv.removeChild(document.getElementById('recordButton'));


    //add label with welcome <name>
    let para = document.createElement("input");
    para.setAttribute("type", "text");
    para.setAttribute("id", "userInput");
    para.setAttribute("placeholder", "Player Name");
    controlsDiv.insertBefore(para, controlsDiv.childNodes[0]);
	
	removeChildDiv(recordsDiv);
	removeChildDiv(historyDiv)

    sessionStorage.clear();
}
//remove children from div
function removeChildDiv(aDiv){
	while(aDiv.firstChild){
		aDiv.removeChild(aDiv.firstChild);
	}
	
}
	

//reset board
function resetBoard(){
 
    for(let [key, value] of moveHistory){
        let aRow = document.getElementById(value);
        let aCard = document.getElementById(key);
        aRow.append(aCard);
        moveHistory.delete(key);
    }
    //clear out Players move.
    playerMove.weapon.location = "";
    playerMove.weapon.name = "";
    playerMove.suspect.location = "";
    playerMove.suspect.name = "";
}

//guess function
function makeGuess(){
    //check if p tag for guess description
    gPara = document.getElementById("moveDescr");
    console.log(gPara);
    if(gPara == null){
        var gPara = document.createElement('p');
        gPara.setAttribute("id", "moveDescr");
    }
    if(gPara.hasChildNodes()){
        gPara.removeChild(gPara.childNodes[0]);
    }
    
    if(moveHistory.size > 2 || playerMove.weapon.location != playerMove.suspect.location || playerMove.weapon.location ==""){
        //invalid guess
        addPara("Your have preformed an invalid move! Try again.", gPara, controlsDiv);
        //reset board
        resetBoard();
        return;
    }
    if(isMatch()){
        message = player.name + " wins! It was: " + secretTuple.suspect + 
        " in the " + secretTuple.room + 
        " with the "  + secretTuple.weapon;
        addPara(message, gPara, controlsDiv);
        //use continue to start new game
        document.getElementById("guessButton").disabled = true;
        document.getElementById("continueButton").disabled = false;
        gameOver = true;
        //set player to win and store in local storage
        player.outcome = "Win"
        localStorage.setItem(date, JSON.stringify(player));

    }else{

        message = "Incorrect guess! Computer has the " +  
        opponetsCard([playerMove.weapon.name, playerMove.suspect.name, playerMove.suspect.location], ComputerArray) + " card.";
        addPara(message, gPara, controlsDiv);
        //enable continue and disabel submit
        document.getElementById("guessButton").disabled = true;
        document.getElementById("continueButton").disabled = false;
        //store guess in session storage
        guessCount +=1;
        snPlayer = player.name + " guess " + guessCount;
        sessionStorage.setItem(snPlayer, JSON.stringify(playerMove));
    }
}

//computers move
function computerGuess(){
    //start game over if there was a winner
    if(gameOver){
        resetGame();
        return;
    }
    //reset cards
    resetBoard();
    //check if p tag for guess description
    gPara = document.getElementById("moveDescr");
    console.log(gPara);
    if(gPara == null){
        //console.log('adding gPara');
        var gPara = document.createElement('p');
        gPara.setAttribute("id", "moveDescr");
    }
    if(gPara.hasChildNodes()){
        gPara.removeChild(gPara.childNodes[0]);
    }
    //Set computer playMove cards
    playerMove.suspect.location = computerGuessArray.room[Math.floor(Math.random()*computerGuessArray.room.length)].toLowerCase().replace(' ', '');
    playerMove.weapon.location = playerMove.suspect.location;
    playerMove.suspect.name = computerGuessArray.suspect[Math.floor(Math.random()*computerGuessArray.suspect.length)].toLowerCase().replace(' ', '');
    playerMove.weapon.name = computerGuessArray.weapon[Math.floor(Math.random()*computerGuessArray.weapon.length)].toLowerCase().replace(' ', '');
    moveHistory.set(playerMove.suspect.name, "suspectRow");
    moveHistory.set(playerMove.weapon.name, "weaponRow");
    //append to room
    document.getElementById(playerMove.suspect.location).append(document.getElementById(playerMove.suspect.name));
    document.getElementById(playerMove.weapon.location).append(document.getElementById(playerMove.weapon.name));
    //console.log(moveHistory);
    //check for match
    if(isMatch()){
        message = "Computer wins! It was: " + secretTuple.suspect + 
        " in the " + secretTuple.room + 
        " with the "  + secretTuple.weapon;
        addPara(message, gPara, controlsDiv);
        gameOver = true;
        //set player outcome to loss and store in storage
        player.outcome = "Loss"
        localStorage.setItem(date, JSON.stringify(player));
    } else{
        let opntCard = opponetsCard([playerMove.weapon.name, playerMove.suspect.name, playerMove.suspect.location], playerArray);
        message = "Incorrect guess! "+ player.name + " has the " + opntCard + " card.";
        addPara(message, gPara, controlsDiv);
        computerChoices(opntCard);
        //enable guess and disabel continue
        document.getElementById("guessButton").disabled = false;
        document.getElementById("continueButton").disabled = true;
        //store guess in session storage
        snPlayer = "Computer guess " + guessCount;
        sessionStorage.setItem(snPlayer, JSON.stringify(playerMove));

    }
    
}
//display paragraph
function addPara(message, para, div){
    let pNode = document.createTextNode(message);
    para.append(pNode);
    div.append(para);
}
//check match
function isMatch(){
    return (secretTuple.weapon.replace(' ', '').toLowerCase() == playerMove.weapon.name.toLowerCase() && 
        secretTuple.suspect.replace(' ', '').toLowerCase() == playerMove.suspect.name.toLowerCase() && 
        secretTuple.room.replace(' ', '').toLowerCase() == playerMove.suspect.location.toLowerCase());
}

//check opponents dech and return a guessed card
function opponetsCard(guessCards, opntCards){
    let hasCard = [];
    //console.log(guessCards);
    //console.log(opntCards);
    opntCards.forEach(card =>{
        //console.log(card);
        if(guessCards.includes(card.replace(' ', '').toLowerCase())){
            hasCard.push(card);
        }
    });
    return getRandomCard(hasCard);
}
//remove know card from computers deck
function computerChoices(cards){
    cards.forEach(card =>{
        if(computerGuessArray.room.includes(card)){
            computerGuessArray.room.splice(computerGuessArray.room.indexOf(card),1);
        }else if (computerGuessArray.weapon.includes(card)){
            computerGuessArray.weapon.splice(computerGuessArray.weapon.indexOf(card),1);
        }else if (computerGuessArray.suspect.includes(card)){
            computerGuessArray.suspect.splice(computerGuessArray.suspect.indexOf(card),1);
        }
    });
}

//random card gen
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
async function divideCards(){
    //combined the decks
    let deckArray = weaponsArray.concat(suspectArray).concat(roomArray);
    //shuffle and distrabute
    while(deckArray.length > 0){
        let cCard = getRandomCard(deckArray);
        let pCard = getRandomCard(deckArray);
        let values = await Promise.all([cCard, pCard]);
        ComputerArray.push(values[0].toString());
        playerArray.push(values[1].toString());     
    }
}

//history
//show previous moves
function viewMoves(){
    //todo
    let moves = document.getElementById("historyButton");
    if(moves.getAttribute("value") == "show"){
        //console.log(sessionStorage);
        moves.innerHTML = "Hide History";
        moves.setAttribute("value", "hide");

        let p = document.createElement("p");
        p.setAttribute("id", "playerMove");
        historyDiv.append(p);
        for(let i = 0; i < sessionStorage.length; i++){
            let data = sessionStorage.getItem(sessionStorage.key(i));
            let aMove = JSON.parse(data);
            document.getElementById("playerMove").innerHTML += sessionStorage.key(i) + "- " + 
                                                                aMove.suspect.name + " in the " + 
                                                                aMove.suspect.location + " with a " +
                                                                aMove.weapon.name +  "<br>";
        }

    }else{
        //console.log("hide session storage");
        moves.innerHTML = "Show History";
        moves.setAttribute("value", "show");
        historyDiv.removeChild(document.getElementById("playerMove"));
    }
    
}
//show win/loss record
function viewRecord(){
    //todo
    let records = document.getElementById("recordButton");
    if(records.getAttribute("value") == "show"){
        records.innerHTML = "Hide Record";
        records.setAttribute("value", "hide");
        let lossCount = 0;
        let p = document.createElement("p");
        p.setAttribute("id", "playerRecord");
        recordsDiv.append(p);
        for(let i = 0; i < localStorage.length; i++){
            let data = localStorage.getItem(localStorage.key(i));
            let aPlayer = JSON.parse(data);
            document.getElementById("playerRecord").innerHTML += aPlayer.name + " " + aPlayer.date + " " + aPlayer.outcome +  "<br>";
            if(aPlayer.outcome == "Loss"){
                lossCount +=1;
            }
        }
        document.getElementById("playerRecord").innerHTML = "Computer Won: " + lossCount + " of " + localStorage.length +  
        " games. <br>" + document.getElementById("playerRecord").innerHTML;

    }else{
        //console.log("hide record storage");
        records.innerHTML = "Show Record";
        records.setAttribute("value", "show");
        recordsDiv.removeChild(document.getElementById("playerRecord"));
    }
}