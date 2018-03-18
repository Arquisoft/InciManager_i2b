"use strict";

var names = ["Chung", "Ellie", "Denis", "Karoline", "Edwardo", "Julia"];

function generateRandomName() {
    var index = Math.floor(Math.random() * names.length);
    return names[index];
}

class ChatRoom {
    constructor() {
        this.bot = new ChatBot(this, generateRandomName());
    }

    createUserMessage(message) {
        $(".chat-ul").append("<li><div class='message-data'><span class='message-data-name'>"
         + "<i class='fa fa-circle you'></i>You</span></div><div class='message you-message'>"
         + message + "</div></li>");
    }

    createBotMessage(message) {
        $(".chat-ul").append("<li><div class='message-data align-right'>"
         + "<span class='message-data-name'>" + this.bot.name + ", you InciManager assistant"
         + "</span><i class='fa fa-circle me'></i><div><div class='message me-message "
         + "float-right'>" + message + "</div></li>");
    }

    onNewUserMessage() {
        var message = $("textarea").val();
        if (message != "") {
            this.createUserMessage(message);
            this.bot.onNewUserMessage(message);

            // remove last message from textarea
            $("textarea").val("");
        }
    }
}

var chat = new ChatRoom();
