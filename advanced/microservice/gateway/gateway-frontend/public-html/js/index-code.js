/*
    Note: very, very basic (and bad) JS code, with no validations, nor exception handling.
    Ie, a typical example of quick&dirty code...
*/

const sendToZuul =  (idText, idMessages) => {

    const textArea = document.getElementById(idText);

    const post = new XMLHttpRequest();
    post.open("POST", "/service/messages", true);
    post.setRequestHeader("Content-type", "text/plain");

    post.onload = function () {
        updateMessages(idMessages);
    };

    post.send(textArea.value);

    textArea.value = "";
};

const deleteMessages = (idMessages) => {

    const del = new XMLHttpRequest();
    del.open("DELETE", "/service/messages", true);

    del.onload = function () {
        updateMessages(idMessages);
    };

    del.send();
};

const updateMessages = (idMessages) => {

    const get = new XMLHttpRequest()
    get.open("GET", "/service/messages", true);
    get.setRequestHeader("Accept", "application/json;charset=UTF-8");

    get.onload = function () {
        const messages = JSON.parse(this.responseText);

        const msgDiv = document.getElementById(idMessages);

        msgDiv.innerHTML = '';

        let list = "<ul>";

        messages.forEach(function (item, index) {
            //Note: security-wise, this is really bad, as open to XSS attacks
            list += "<li>" + item.system + " : " + item.message +"</li>";
        });

        list += "</ul>";

        msgDiv.innerHTML = list;
    };

    get.send();
};