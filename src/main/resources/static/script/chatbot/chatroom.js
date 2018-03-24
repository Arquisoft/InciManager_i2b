"use strict";

// random names of the bot to be created :)
var names = ["Chung", "Ellie", "Albert", "Karoline", "Brendan", "Julia"];
var chat;

/*
 * Generates a random name from the global list of names.
 */
function generateRandomName() {
    var index = Math.floor(Math.random() * names.length);
    return names[index];
}

class ChatRoom {
    constructor() {
        / global ChatBot /
        this.bot = new ChatBot(this, generateRandomName());
        this.bot.welcomeMessage();
    }

    /*
     * Creates the html structure and classes of a user response
     * (e.g. grey color, float left...), introducing the message
     * received in this structure, and appends this message to the end
     * of the chat history.
     */
    createUserMessage(message) {
        $(".chat-ul").append("<li><div class='message-data'><span class='message-data-name'>"
         + "<i class='fa fa-circle you'></i>You</span></div><div class='message you-message'>"
         + message + "</div></li>");

         // auto scroll scrollbar
         this.scrollDown();
    }

    /*
     * Creates the html structure and classes of a bot response
     * (e.g. orange color, float right...), introducing the message
     * received in this structure, and appends this message to the end
     * of the chat history.
     */
    createBotMessage(message) {
        $(".chat-ul").append("<li class='clearfix'><div class='message-data align-right'>"
         + "<span class='message-data-name'>" + this.bot.name + ", your InciManager assistant"
         + "</span><i class='fa fa-circle me'></i></div><div class='message me-message "
         + "float-right'>" + message + "</div></li>");

        this.scrollDown();
    }

    /*
     * Callback to be called when a new user message is sent
     * through the html view. The user message is obtained
     * from the textarea, appended to the end of the chat
     * history, and sent to the bot for its processing.
     */
    onNewUserMessage() {
        var message = $("textarea").val();
        if (message !== "") {
            this.createUserMessage(message);
            this.bot.onNewUserMessage(message);

            // remove last message from textarea
            $("textarea").val("");
        }
    }

    scrollDown() {
        $(".chat-history").scrollTop($(".chat-history")[0].scrollHeight);
    }
}

$(document).ready(function() {
    chat = new ChatRoom();

    // allow enter button to send the current message inside the text area
    $("#textArea").keypress(function (e) {
        var code = (e.keyCode ? e.keyCode : e.which);
        if (code === 13) {
            $("#sendMsgButton").trigger("click");
            return false;
        }
    });
});
