

function formatTimestampToDay (date) {
	var date = new Date(date);
	var Y = date.getFullYear();  // 获取完整的年份(4位,1970)
	var M = date.getMonth() + 1;  // 获取月份(0-11,0代表1月,用的时候记得加上1)
	var D = date.getDate();  // 获取日(1-31)
	return Y + '-' + M + '-' + D
}

function formatTimestampToSecond (date) {
	var date = new Date(date);
	var Y = date.getFullYear();  // 获取完整的年份(4位,1970)
	var M = date.getMonth() + 1;  // 获取月份(0-11,0代表1月,用的时候记得加上1)
	var D = date.getDate();  // 获取日(1-31)
	var H = date.getHours();
	var m = date.getMinutes();
	var S = date.getSeconds();
	return Y + '-' + M + '-' + D + ' ' + H + ':' + m + ':' + S
}

// 毫秒转为日时分秒
function MillisecondToDate(msd) {
	var second_time = parseInt(msd / 1000)
	var time = parseInt(second_time) + "秒";  
	if( parseInt(second_time )> 60){  
	  
	    var second = parseInt(second_time) % 60;  
	    var min = parseInt(second_time / 60);  
	    time = min + "分" + second + "秒";  
	      
	    if( min > 60 ){  
	        min = parseInt(second_time / 60) % 60;  
	        var hour = parseInt( parseInt(second_time / 60) /60 );  
	        time = hour + "小时" + min + "分" + second + "秒";  
	  
	        if( hour > 24 ){  
	            hour = parseInt( parseInt(second_time / 60) /60 ) % 24;  
	            var day = parseInt( parseInt( parseInt(second_time / 60) /60 ) / 24 );  
	            time = day + "天" + hour + "小时" + min + "分" + second + "秒";  
	        }  
	    }  	      
	}  
	return time;
}