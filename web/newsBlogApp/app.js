var http = require("http");
var url = require("url");
var qs = require("querystring");
const NewsService = require("./NewsService.js");
const { join } = require("path");
const { get } = require("https");
var cookies = [];
var newsStory = require("./NewsService.js").NewsStory;
var newsService = require("./NewsService.js").NewsService;
var htmlHeader =
  '<!DOCTYPE html><html lang="en"><head><meta charset="UTF-8" /> \
                <meta name="viewport" content="width=device-width, initial-scale=1.0" /> \
                <title>tnielse3 lab2</title> </head><body>';
var htmlFooter = "</body></html>";

const currentUser = {
  name: "",
  type: "",
  login: false,
  storyID: "",
};
var serviceLibrary = new newsService();

//seed data
serviceLibrary.newStory(
  '{"author": "ThorNielsen","title": "Title 1","content": "Content 1","flag": true,"date": "2020-9-25"}'
);
serviceLibrary.newStory(
  '{"author": "nielThor","title": "Title 1","content": "Content 1","flag": true,"date": "2018-10-31"}'
);
serviceLibrary.newStory(
  '{"author": "Thor","title": "Title 1","content": "Content 1","flag": true,"date": "2019-8-20"}'
);
serviceLibrary.newStory(
  '{"author": "Gary","title": "Title 1","content": "Content 1","flag": false,"date": "2020-9-15"}'
);
serviceLibrary.newStory(
  '{"author": "Bill","title": "Title 1","content": "Content 1","flag": false,"date": "2020-1-29"}'
);
serviceLibrary.newStory(
  '{"author": "Thor","title": "Title 2","content": "Content 2","flag": false,"date": "2020-8-20"}'
);
serviceLibrary.newStory(
  '{"author": "Gary","title": "Title 2","content": "Content 2","flag": true,"date": "2020-9-18"}'
);

//This is my server for all reqyests
http
  .createServer((req, res) => {
    var pageData = "";
    var pageParms = "";

    if (req.method == "POST") {
      req.on("data", (chunk) => {
        pageData += chunk;
      });
      req.on("end", () => {
        console.log(req.url);
        pageParms = qs.parse(pageData);

        if (req.url == "/viewnews") {
          if (
            pageParms.userName == pageParms.password &&
            pageParms.userName != "" &&
            pageParms.password != ""
          ) {
            currentUser.name = pageParms.userName;
            currentUser.type = pageParms.userType;
            currentUser.login = true;
            cookies.push(
              "username=" +
                pageParms.userName +
                "; expires=Tue, 10 Dec 2020 03:14:07 GMT"
            );
            cookies.push(
              "usertype=" +
                pageParms.userType +
                "; expires=Tue, 10 Dec 2020 03:14:07 GMT"
            );
            cookies.push("login=true");

            res.setHeader("set-cookie", cookies);
            res.writeHead(200);
            res.write(getStories());
            res.end();
          } else {
            res.writeHead(302, { Location: "/loginfail" });
            res.end();
          }
        } else if (req.url == "/createstory") {
          console.log("in the post for create stroy");
          let aStory = {
            author: pageParms.author,
            title: pageParms.title,
            flag: true,
            content: pageParms.content,
            date: pageParms.date,
          };
          if (pageParms.flag == undefined) {
            aStory.flag = false;
          }
          console.log(serviceLibrary.newStory(JSON.stringify(aStory)));
          if (serviceLibrary.newStory(JSON.stringify(aStory))) {
            res.writeHead(302, { Location: "/viewnews" });
            res.end();
          } else {
            res.writeHead(501);
            res.write(createStory(currentUser.name, true));
            res.end();
          }
          //serviceLibrary.newStory(JSON.stringify(aStory));
        } else if (req.url == "/delete") {
          let deleteObj = { id: pageParms.id };
          if (serviceLibrary.deleteStory(JSON.stringify(deleteObj))) {
            res.writeHead(302, { Location: "/viewnews" });
            res.end();
          } else {
            res.writeHead(501);
            res.write(singleStory(currentUser.storyID, true));
            res.end();
          }
        }
      });
    } else if (req.method == "GET") {
      var urlObj = url.parse(req.url, true, false);
      var qstr = urlObj.query;
      console.log(qstr);

      if (req.headers.cookie != undefined) {
        console.log("cookie string 1->" + req.headers.cookie);
        let getCookies = req.headers.cookie.split(";");
        getCookies.forEach((element) => {
          if (element.split("=")[0] == "username") {
            currentUser.name = element.split("=")[1];
            console.log(currentUser.name);
          } else if (element.split("=")[0] == "usertype") {
            console.log(currentUser.type);
            currentUser.type = element.split("=")[1];
          }
        });
        if (getCookies.length >= 3) {
          currentUser.login = true;
        } else {
          currentUser.login = false;
        }
      }
      if (!currentUser.login) {
        res.writeHead(200);
        res.write(getLoginView(currentUser.name, currentUser.type));
        res.end();
      } else if (req.url == "/") {
        let cookieUser = "";
        if (req.headers.cookie != undefined) {
          console.log("cookie string ->" + req.headers.cookie);
          cookieUser = req.headers.cookie.split("=")[1];
        }
        res.writeHead(200);
        res.write(getLoginView(currentUser.name, currentUser.type));
        res.end();
      } else if (req.url == "/viewnews") {
        res.writeHead(200);
        res.write(getStories());
        res.end();
      } else if (req.url == "/createstory") {
        res.writeHead(400);
        res.write(createStory(currentUser.name));
        res.end();
      } else if (req.url == "/singlestory?id=" + qstr.id) {
        let storyFound = false;
        currentUser.storyID = qstr.id;
        let storyContent = singleStory(qstr.author, qstr.title);
        serviceLibrary.newStoryArray.forEach((element) => {
          if (qstr.id == element.id) {
            storyFound = true;
            res.writeHead(200);
            res.write(singleStory(qstr.id));
            res.end();
          }
        });
        if (!storyFound) {
          res.writeHead(404);
          res.write(getUnknownPage());
          res.end();
        }
      } else if (req.url == "/loginfail") {
        res.writeHead(401);
        res.write(getLoginFail());
        res.end();
      } else {
        console.log(req.url);
        res.writeHead(404);
        res.write(getUnknownPage());
        res.end();
      }
    } else {
      console.log("I fell out");
      res.writeHead(200);
      res.write(getLoginView(currentUser.name, currentUser.type));
      res.end();
    }
  })
  .listen(3000);

//Unknown page
function getUnknownPage() {
  let unknownPage =
    htmlHeader +
    '<h1>Unknown Page 404</h1> \
  <a href="http://localhost:3000">Login</a>' +
    htmlFooter;
  return unknownPage;
}

//Main login page
function getLoginView(user, userType) {
  var loginView = htmlHeader;
  if (user != "") {
    loginView +=
      "<h1>Welcome " +
      user +
      " : " +
      userType +
      '</h1><form action="http://localhost:3000/viewnews" method="POST"> \
       <label for="userName">User Name: </label><br> \
       <input type="text" id="userName" name="userName" value="' +
      user +
      '"><br>';
  } else {
    loginView +=
      '<form action="http://localhost:3000/viewnews" method="POST"> \
    <label for="userName">User Name: </label><br> \
    <input type="text" id="userName" name="userName" placeholder="username"><br>';
  }
  loginView +=
    '<label for="password">Password: </label><br> \
    <input type="password" id="password" name="password" placeholder="password"><br><br> \
    <label for="guest">Guest: </label> \
    <input type="radio" id="guest" name="userType" value="guest" checked> \
    <label for="author">Author: </label> \
    <input type="radio" id="author" name="userType" value="author"> \
    <label for="subscriber">Subscriber: </label> \
    <input type="radio" id="subscriber" name="userType" value="subscriber" > <br><br>\
  <input type="submit" value="Login"> </form>' +
    htmlFooter;

  return loginView;
}

//login fail page
function getLoginFail() {
  let loginFail =
    htmlHeader +
    '<h1>LOGIN FAILED</h1> \
  <a href="http://localhost:3000">RETURN TO LOGIN PAGE</a>' +
    htmlFooter;
  return loginFail;
}

//This function will return the stroy as links or titles
function getStories() {
  let storyTitle = htmlHeader;
  storyTitle += "<h4>" + currentUser.type + ": " + currentUser.name + "</h4>";
  storyTitle += '<a href="http://localhost:3000">logout </a><br>';
  if (currentUser.type == "author") {
    storyTitle +=
      '<a href="http://localhost:3000/createstory">New Story</a><br>';
  }
  storyTitle += "<br>";
  serviceLibrary.newStoryArray.forEach((element) => {
    if (
      element.publicFlag == true ||
      currentUser.type == "subscriber" ||
      (currentUser.type == "author" && element.author == currentUser.name)
    ) {
      storyTitle +=
        '<a href="http://localhost:3000/singlestory?id=' +
        element.id +
        '">' +
        element.storyTitle +
        "</a><br>";
    } else {
      storyTitle += "<label>" + element.storyTitle + "</label><br>";
    }
  });
  return storyTitle + htmlFooter;
}

//This function will create a story
function createStory(aUser, failedToCreate) {
  let newStory = htmlHeader;
  if (failedToCreate) {
    newStory += "<h1>FAILD TO CREATE! TRY AGAIN</h1>";
  }
  newStory +=
    '<form action="http://localhost:3000/createstory" method="POST"> \
  <h1>Create New Story</h1> \
  <label for="author">Author</label><br /> \
  <input \
    type="text" \
    id="author" \
    name="author" \
    value="' +
    aUser +
    '" \
    required \
  /><br /> \
  <label for="date">date</label><br /> \
  <input type="date" id="date" name="date" required /><br /> \
  <label for="title">Title</label><br /> \
  <input type="text" id="title" name="title" required /><br />\
  <label for="content">Article    </label> \
  <input type="checkbox" name="flag" value="true"> \
  <label for="flag">Public</label><br /> \
  <textarea id="content" name="content" required></textarea><br /> \
  <input type="submit" value="Save" /> \
  <a href="http://localhost:3000/viewnews">Cancel</a></form>';
  newStory += htmlFooter;

  return newStory;
}

//This function will return single story page
function singleStory(id, notDeleted) {
  let storyElement = {
    author: "",
    title: "",
    content: "",
    date: "",
  };
  let isAuthor = false;
  let aStory = htmlHeader;
  serviceLibrary.newStoryArray.forEach((element) => {
    if (id == element.id) {
      storyElement.author = element.author;
      storyElement.title = element.storyTitle;
      storyElement.content = element.storyContent;
      storyElement.date = element.date;
      isAuthor = storyElement.author == currentUser.name;
    }
  });
  if (isAuthor && currentUser.type == "author") {
    aStory += '<form action="http://localhost:3000/delete" method="POST">';
  }
  if (notDeleted != undefined && notDeleted == true) {
    aStory += "<h1>STORY FAILED TO DELETE</h1>";
  }

  aStory += "<h6>" + storyElement.author + " - " + storyElement.date + "</h6>";
  aStory += "<p>" + storyElement.content + "</p>";
  aStory += '<a href="http://localhost:3000/viewnews">Retun </a><br><br>';
  aStory += '<input type="hidden" id="id" name="id" value= "' + id + '">';
  if (isAuthor && currentUser.type == "author") {
    aStory += '<input type="submit" value="Delete"> </form>';
  }
  aStory += htmlFooter;
  return aStory;
}

//This function will allow the creation of a story

console.log("Listening on 3000");
