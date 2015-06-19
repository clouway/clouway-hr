function getCurrentDate() {
  var currentDate = new Date();
  var day = addZeroToDate(currentDate.getDate());
  var month = addZeroToDate(currentDate.getMonth() + 1);
  var year = currentDate.getFullYear();

  function addZeroToDate(someVar) {

    if (someVar.toString().length == 1) {
      return '0' + someVar;
    }
    return someVar;
  }

  return day + '-' + month + '-' + year;
}

function getCurrentDate2() {
  var monthNames = [
    "January", "February", "March",
    "April", "May", "June", "July",
    "August", "September", "October",
    "November", "December"
  ];

  var date = new Date();
  var day = date.getDate();
  var monthIndex = date.getMonth();
  var year = date.getFullYear();
  if(day.toString().length==1){
    day='0'+day;
  }
  return day + '-' + monthNames[monthIndex] + '-' + year;
}

function validate(evt) {
  var theEvent = evt || window.event;
  var key = theEvent.keyCode || theEvent.which;
  key = String.fromCharCode(key);
  var regex = /[0-9]|\./;

  if (!regex.test(key)) {
    theEvent.returnValue = false;
    if (theEvent.preventDefault) theEvent.preventDefault();
  }
}

$(function(){
  var navMain = $("#bs-example-navbar-collapse-1");

  navMain.on("click", "a", null, function () {
    navMain.collapse('hide');
  });
});