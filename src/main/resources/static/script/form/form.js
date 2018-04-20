"use strict";

$(document).ready(function () {
    document.getElementById("inciName").focus();
});

var incident = {
    "inciName" : "",
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

function deleteTag(index) {
    form.deleteTag(index);
}

function deleteProperty (index) {        
    form.deleteProperty(index);
}

class Form {
    
    constructor (){
        this.geoCoord = {};
        this.nTags = 1;
        this.nProperties = 1;
        this.info = "";
    }
    
    setLocationField (fieldName){
        switch (fieldName){
            case "latitude":
                $("#latitude").val(form.geoCoord.lat);
                $("#latitude").prop('disabled', true);
                break;

            case "longitude":
                $("#longitude").val(form.geoCoord.lon);
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
        "type='text' id='tag"+ this.nTags +"'placeholder='Emergency' name='tags'>&#160;"+
        "<button id='deleteTag"+ this.nTags +"' type='button' class='deletionTag' "+
        "onclick='deleteTag("+ this.nTags +")' >"+
        "<i class='fa fa-times-circle'></i></button>&#160;</div>");
        
        this.nTags++;
    }
    
    addProperty () {
        $("#properties").append("<div id='property-group"+ this.nProperties +
        "' class='animated fadeIn'>"+
        "<input class='propertyName form-control' type='text' id='propertyName"+ this.nProperties + "'placeholder='Heigth'>&#160;"+
        "<input class='propertyValue form-control' type='text' id='propertyValue"+ this.nProperties +"' placeholder='1000 metres'>"+
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
    
    setIncident () {
        
        
        incident.inciName = $("#inciName").val().trim();

        incident.location.lat = $("#latitude").val().trim();
        incident.location.lon = $("#longitude").val().trim();
        
        //tags
        
        $(".tagInput").each (function () {
            if ($.trim($(this).val()) != ""){
                incident.tags.push($(this).val());
            };
        });
        
        //properties
        
        var names = [];
        var values = [];
        $(".propertyName").each (function () {
            names.push($(this).val());
        });
        
        $(".propertyValue").each (function () {
            values.push($(this).val());
        });
        
        var i;
        for (i = 0; i < names.length; i++) { 
            if ($.trim(names[i]) != ""){
                incident.properties[names[i]] = values[i];
            }
        };
        
        
        //moreInfo
        if ($.trim($("#moreInfo").val()) != ""){
            incident.moreInfo.push($("#moreInfo").val()); 
        }
        
        console.log (JSON.stringify(incident));

    }
    
    validateIncident (){
        
        var inciName = $.trim($("#inciName").val());
        var latitude = $("#latitude").val();
        var longitude = $("#longitude").val();
        
        //InciName
        if (!$.trim($("#inciName").val())){
            $("#errorName").removeAttr("hidden");
            return false;
         }
        else {
            $("#errorName").prop("hidden", "true");
        }
        
        if (!$.isNumeric(latitude) || !$.isNumeric(longitude)
                || latitude < -90 || latitude > 90 
                || longitude < -180 || longitude > 180){
            $("#errorLocation").removeAttr("hidden");
            return false;
        }
        else {
            $("#errorLocation").prop("hidden", "true");
        }
        
        $("#submitBtn").unbind();
        return true;
        
    }
    
    createIncident () {
    	
        var url = window.location.href;
        var arr = url.split("/");

        $.ajax({
            type: 'POST',
            url: arr[0] + "//" + arr[2] + "/incident/create",
            contentType: "application/json; charset=utf-8",
            datatype: "json",
            data: JSON.stringify(incident),
            success: function(data) {
                
                window.location.replace(arr[0] + "//" + arr[2] + "/incidents");
                
            }.bind(this), error: function() {
                $("#errorMsg").removeAttr("hidden");

                
            }.bind(this)
        });
    }
    
    submit(){
        if (!form.validateIncident()){
            $("#errorMsg").removeAttr("hidden");
            $("#submitBtn").click (function () {
                form.submit();    
            incident.tags = [];
            incident.location ={};
            incident.moreInfo = [];
            incident.properties = {};
         });
        }
        
        else {
            $("#errorMsg").prop("hidden", "true");
            form.setIncident();
            form.createIncident();
         }
        
    }
}
    

var form = new Form();

$("#autoLocation").click(function(){
    
    console.log ("Current latitude: " + form.geoCoord.lat);
    console.log ("Current longitude: " + form.geoCoord.lon);
    
    
    if (typeof form.geoCoord.lat  === "undefined"){
        $("#locating").prop ('hidden', false);
        $("#locating").fadeIn();
        
        navigator.geolocation.getCurrentPosition(function(position) {
                    
        // Get coordinates
        form.geoCoord = {
            lat : position.coords.latitude,
            lon : position.coords.longitude
        }
            
        form.setLocationField("latitude");
        form.setLocationField("longitude");
            
        $("#locating").fadeOut();
    });
        
    }
    
    else {
    
        console.log ("Parsed latitude: " + form.geoCoord.lat);
        console.log ("Parsed longitude: " + form.geoCoord.lon);

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
    
    var id = $(this).attr('id').toString();
    var index = id.charAt(id.length-1);
    form.deleteProperty(index);
    
});

$("#submitBtn").click (function () {
        form.submit();        
});