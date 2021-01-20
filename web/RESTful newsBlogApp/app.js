var express = require('express'),
    bodyParser = require('body-parser'),
    expressSanitizer = require('express-sanitizer'),
    NewsService = require('./NewsServiceAPI'),
    fs = require('fs'),
    key = fs.readFileSync('./key.pem'),
    cert = fs.readFileSync('./cert.pem'),
    https = require('https'),
    http = require('http'),
    newsService = new NewsService(),
    app = express();

const httpsServer = https.createServer({key: key, cert: cert }, app);
const httpServer = http.createServer(app);
    app.use(bodyParser.urlencoded({ extended: true }));
    app.use(expressSanitizer());
    app.use(bodyParser.json());
    

    //route gets
    app.get('/stories/id/:id', (req, res)=>{
        let qryStr = {};
        if(req.params.id != undefined){
            qryStr.id = req.params.id;
            let newsStories = newsService.getStoriesForFilter(qryStr);
            res.send(JSON.stringify(newsStories));
        }else{
            res.writeHead(400);
            res.send("Bad Reqest");
        }
    });

    app.get('/stories', (req, res)=>{
        let qryStr = req.query;
        console.log(qryStr);
        let dateRange = {};
        if(qryStr.startDate != undefined){
          dateRange.startDate = new Date(parseInt(qryStr.startDate)).toString();
          delete qryStr.startDate;
        }
        if(qryStr.endDate != undefined){
          dateRange.endDate = new Date(parseInt(qryStr.endDate)).toString();
          delete qryStr.endDate;
       }
       if(Object.entries(dateRange).length != 0){
            qryStr.dateRange = dateRange;
       }
     
        let newsStories = newsService.getStoriesForFilter(qryStr);
        console.log(newsStories);
        res.send(JSON.stringify(newsStories));

    });
    //route post
    app.post('/stories', (req, res)=>{
        //new story
        let title = req.sanitize(req.body.title);
        let content = req.sanitize(req.body.content);
        let author = req.sanitize(req.body.author);
        let flag = req.sanitize(req.body.flag);
        let date = req.sanitize(req.body.date);

        if(title == undefined || 
            content == undefined || 
            author == undefined || 
            flag == undefined || 
            date == undefined){
                res.writeHead(422);
        }else{
            let isPublic = (flag == 'true');
            let storyId = newsService.addStory(title, content, author, isPublic, date);
            res.send(JSON.stringify({'storyId': storyId}));
        }
    });
    //route put
    app.put('/stories/:id', (req, res)=>{
        let newsStories;
        let title = req.sanitize(req.body.title);
        let content = req.sanitize(req.body.content);
        //console.log(req.params.id + " " + title); 
        //check if update is tite
        if(req.params.id != undefined && title  != undefined){
            newsService.updateTitle(req.params.id, title);
            newsStories = newsService.getStoriesForFilter({'id': req.params.id});
        }
        //check if udate is content
        else if(req.params.id != undefined && content != undefined){
            newsService.updateContent(req.params.id, content);
            newsStories = newsService.getStoriesForFilter({'id': req.params.id});    
        }
        //422 if body is incorrect
        else{
            res.writeHead(422);
            res.send();
        }
        //send the update
        res.send(newsStories);
    });
    //route delete
    app.delete('/stories/:id', (req, res)=>{
        if(req.params.id == undefined){
            res.writeHead(422);
            res.send();
        }
        newsService.deleteStory(req.params.id);
        //delete using id
        res.send("Story Delete");
    });

    httpServer.listen(3000, () => {
        console.log("Listening on port 3000");
      });
    httpsServer.listen(3012, () => { console.log('listening on 3012') });