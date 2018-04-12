"use strict";

$(document).ready(function() {
    document.getElementById("inciName").focus()
});

var incident = {
    "agent": {
        "username": agentInfo.username,
        "password": agentInfo.password,
        "kind": agentInfo.kind
    },
    "location": {

    },
    "tags": [],
    "moreInfo": [],
    "properties": {

    }
};

function deleteTag(index){
    form.deleteTag(index);
}

function deleteProperty(index){
    form.deleteProperty(index);
}

class Form {
    
    constructor (){
        this.geoCoord = {};
        this.nTags = 1;
        this.nProperties = 1;
        this.info = "";
    }
    
    setLocationField (fieldName)
    {
        switch (fieldName){
            case "latitude":
                $("#latitude").val(form.geoCoord.lat);
                $("#latitude").prop('disabled', true);
                break;

            case "longitude":
                $("#longitude").val(form.geoCoord.long);
                $("#longitude").prop('disabled', true);
                break;

            default:
                break;
        }
    }
    
    addTag () {
        
        $("#tags").append("<div id='tag-group"+ this.nTags +
        "' class = 'animated fadeIn'>"+
        "<input class='tagInput form-control' "+
        "type='text' id='tag"+ this.nTags +"'placeholder='Emergency'>&#160;"+
        "<button id='deleteTag"+ this.nTags +"' type='button' class='deletionTag' "+
        "onclick='deleteTag("+ this.nTags +")' >"+
        "<i class='fa fa-times-circle'></i></button>&#160;</div>");
        
        this.nTags++;
    }
    
    addProperty () {
        $("#properties").append("<div id='property-group"+ this.nProperties +
        "' class='animated fadeIn'>"+
        "<input class='propertyInput form-control' type='text' id='propertyName"+ this.nProperties + "'placeholder='Heigth'>&#160;"+
        "<input class='propertyInput form-control' type='text' id='propertyValue"+ this.nProperties +"' placeholder='1000 metres'>"+
        "<button id='deleteProperty"+ this.nProperties +"' type='button' class='deletionProperty' onclick='deleteProperty("+ this.nProperties +")'"+
        "><i class='fa fa-times-circle'></i></button>&#160;</div></div>");
        
        this.nProperties++;
    }
    
    deleteTag (index) {
        
        $("#tag-group"+index.toString()).attr ('class', 'animated fadeOut')
        
        
        $("#tag-group"+index.toString()).fadeOut(400, function(){
            $(this).remove();
        });
        
};

        
    
    
    deleteProperty (index) {
        
        $("#property-group"+index.toString()).attr ('class', 'animated fadeOut')

        
        $("#property-group"+index.toString()).fadeOut(400, function(){
            $(this).remove();
        });
        
    }
    
}


    
    


var form = new Form();

$("#autoLocation").click(function(){
    
    console.log ("Current latitude: " + form.geoCoord.lat);
    console.log ("Current longitude: " + form.geoCoord.long);
    
    
    if (typeof form.geoCoord.lat  === "undefined"){
        $("#locating").prop ('hidden', false);
        $("#locating").fadeIn();
        
        navigator.geolocation.getCurrentPosition(function(position) {
                    
        // Get coordinates
        form.geoCoord = {
            lat : position.coords.latitude,
            long : position.coords.longitude
        }
            
        form.setLocationField("latitude");
        form.setLocationField("longitude");
            
        $("#locating").fadeOut();
    });
        
    }
    
    else {
    
        console.log ("Parsed latitude: " + form.geoCoord.lat);
        console.log ("Parsed longitude: " + form.geoCoord.long);

        // Set form values
        form.setLocationField("latitude");
        form.setLocationField("longitude");
    }

    
});

$("#customLocation").click(function(){
    $("#latitude").prop('disabled', false);
    $("#longitude").prop('disabled', false);

    $("#locating").fadeOut();
});

$("#addTag").click(function(){
    
    form.addTag();
});

$("#addProperty").click(function(){
    
    form.addProperty();
});



$(".deletionTag").click(function(){
    console.log("Stated remove tag")
    var id = $(this).attr('id').toString();
    var index = id.charAt(id.length-1);
    form.deleteTag(index);
});

$(".deletionProperty").click(function(){

    console.log("Stated remove property")
    
    var id = $(this).attr('id').toString();
    var index = id.charAt(id.length-1);
    form.deleteProperty(index);
});






    

    
	
