function ts(field,data){
     var data=JSON.parse(data);
     var result=eval("data."+field);
     return result;
}
