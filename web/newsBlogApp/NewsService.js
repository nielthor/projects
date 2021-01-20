var storyID = 0;
class NewsStory {
  constructor() {
    this.author;
    this.storyTitle;
    this.publicFlag;
    this.storyContent;
    this.id;
    this.date;
  }
}
class NewsService {
  constructor() {
    this.newStoryArray = [];
  }

  newStory(jsonStr) {
    let jsonObj = JSON.parse(jsonStr);
    let aStory = new NewsStory();
    storyID += 1;
    //json - {author: str, title: str, flag: bool, content: str, date: str}
    aStory.author = jsonObj.author;
    aStory.storyTitle = jsonObj.title;
    aStory.publicFlag = jsonObj.flag;
    aStory.storyContent = jsonObj.content;
    aStory.id = storyID;
    aStory.date = jsonObj.date;
    this.newStoryArray.push(aStory);
    return true;
  }
  locateIndex(id) {
    let storyIndex = -1;
    this.newStoryArray.forEach((element, index) => {
      if (element.id == id) {
        storyIndex = index;
      }
    });
    return storyIndex;
  }
  updateTitle(jsonStr) {
    let jsonObj = JSON.parse(jsonStr);
    //json - {id: num, title: str}
    let storyindex = this.locateIndex(jsonObj.id);
    if (storyindex > -1) {
      this.newStoryArray[storyindex].storyTitle = jsonObj.title;
      return true;
    }
    return false;
  }
  updateContent(jsonStr) {
    let jsonObj = JSON.parse(jsonStr);
    let storyindex = this.locateIndex(jsonObj.id);
    //json - {id: num, content: str}
    if (storyindex > -1) {
      this.newStoryArray[storyindex].storyContent = jsonObj.content;
      return true;
    }
    return false;
  }
  deleteStory(jsonStr) {
    let jsonObj = JSON.parse(jsonStr);
    let storyindex = this.locateIndex(jsonObj.id);
    //json - {id: num}
    if (storyindex > -1) {
      this.newStoryArray.splice(storyindex, 1);
      return true;
    }
    return false;
  }
  filterStorys(jsonStr) {
    let jsonObj = JSON.parse(jsonStr);
    let filterArray = this.newStoryArray.slice();
    let i = 0;
    // {title: str, dateStart: str, dateStop: str, author: str}
    //Filter by Title
    if (jsonObj.title != "") {
      for (i = filterArray.length; i--; ) {
        if (filterArray[i].storyTitle != jsonObj.title) {
          filterArray.splice(i, 1);
        }
      }
    }
    //Filter by date
    if (jsonObj.dateStart != "" && jsonObj.dateStop != "") {
      //json date to date object
      // if no date was entered make start date min and stop max
      let startDate;
      let stopDate;
      if (jsonObj.dateStart == "") {
        startDate = new Date("1000-12-30");
      } else {
        startDate = new Date(jsonObj.dateStart);
      }
      if (jsonObj.dateStop == "") {
        stopDate = new Date("9999-12-30");
      } else {
        stopDate = new Date(jsonObj.dateStop);
      }
      //start checking filter array
      for (i = filterArray.length; i--; ) {
        //array data to date object
        let aDate = new Date(filterArray[i].date);

        if (aDate < startDate || aDate > stopDate) {
          filterArray.splice(i, 1);
        }
      }
    }
    //Filter by author
    if (jsonObj.author != "") {
      for (i = filterArray.length; i--; ) {
        if (jsonObj.author != "") {
          if (filterArray[i].author != jsonObj.author) {
            filterArray.splice(i, 1);
          }
        }
      }
    }
    return filterArray;
  }
}

module.exports = {
  NewsStory: NewsStory,
  NewsService: NewsService,
};

// var news = new NewsService();
// news.newStory(
//   '{"author": "nielthor", "title": "Title 1", "flag": true, "content": "This is the content", "date": "2020-9-25"}'
// );
// news.newStory(
//   '{"author": "Thor", "title": "Title 2", "flag": false, "content": "This is Private content", "date": "2020-9-20"}'
// );
// news.newStory(
//   '{"author": "Gary", "title": "Title 3", "flag": true, "content": "This is Public content", "date": "2020-9-15"}'
// );
// console.log("-----INIT LOAD-------");
// console.log(news.newStoryArray);
// console.log("-----UPDATED TITLE-------");
// news.updateTitle('{"id": 1, "title": "Great New Title"}');

// console.log(news.newStoryArray);
// console.log("-----UPDATED CONTENT-------");
// news.updateContent('{"id": 1, "content": "I have been edited"}');
// console.log(news.newStoryArray);
// console.log("-----DELETED STORY-------");
// news.deleteStory('{"id": 1}');
// console.log(news.newStoryArray);

// news.newStory(
//   '{"author": "Thor", "title": "Title 3", "flag": true, "content": "New Content", "date": "2020-9-30"}'
// );
// news.newStory(
//   '{"author": "Bob", "title": "Title 1", "flag": true, "content": "Bobs Content", "date": "2018-9-25"}'
// );
// news.newStory(
//   '{"author": "Mike", "title": "Title 2", "flag": true, "content": "Mikes Content", "date": "2020-10-20"}'
// );
// news.newStory(
//   '{"author": "Gary", "title": "Title 1", "flag": true, "content": "This is the content", "date": "2020-9-03"}'
// );
// console.log("-----CURRENT UNFILTERED LIBRARY-------");
// console.log(news.newStoryArray);

// console.log(
//   "-----FILTER A, B, C------- Title 3, 2019-9-20 to 2020-9-30, Gary - only 1"
// );
// console.log(
//   news.filterStorys(
//     '{"title": "Title 3", "dateStart": "2019-9-20", "dateStop": "2020-9-30", "author": "Gary"}'
//   )
// );
// console.log("-----FILTER TITLE------- Title 3");
// console.log(
//   news.filterStorys(
//     '{"title": "Title 3", "dateStart": "", "dateStop": "", "author": ""}'
//   )
// );
// console.log(
//   "-----FILTER DATE------- dates between 2020-9-20 and 2020-9-30 (should be 2)"
// );
// console.log(
//   news.filterStorys(
//     '{"title": "", "dateStart": "2020-9-20", "dateStop": "2020-9-30", "author": ""}'
//   )
// );
// console.log("-----FILTER AUTHOR------- Thor");
// console.log(
//   news.filterStorys(
//     '{"title": "","dateStart": "", "dateStop": "", "author": "Thor"}'
//   )
// );
