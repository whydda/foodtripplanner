// MODE Define
var TRACE_MODE = 3;
var DEBUG_MODE = 2;
var INFO_MODE = 1;
var ERROR_MODE = 0;

//LOG LEVEL
var LOG_LEVER = TRACE_MODE; //trace

var $LOG = {
    trace : function(msg){
        if(3 <= LOG_LEVER){
            console.log(msg);
        }
    },
    debug : function(msg){
        if(2 <= LOG_LEVER){
            console.log(msg);
        }
    },
    info : function(msg){
        if(1 <= LOG_LEVER){
            console.log(msg);
        }
    },
    error : function(msg){
        if(0 <= LOG_LEVER){
            console.error(msg);
        }
    }
};

var $Ajax = {
    /**
     * ajax 통신 공통 함수
     * @param url
     * @param data
     * @param callback
     * @param contentsType
     * @param methodType
     * @param formObj
     */
    call : function(url, data, callback, methodType, isAsync, contentsType, formObj){
        $('.wrap-loading').removeClass('display-none');
        // 1. Ajax 전송
        if($formCheck.isNullStr(formObj)){
            contentsType = $formCheck.isNullStr(contentsType) ? 'application/x-www-form-urlencoded' : contentsType;
            methodType = $formCheck.isNullStr(methodType) ? 'POST' : methodType;

            //타입을 체크해서 JSON 타입으로 만들어 준다.
            if(typeof data == "object"){
                data = {params : JSON.stringify(data)}
            }

            var options = {
                url : url,
                type : methodType,
                data : data,
                contentType : contentsType,
                async : isNull(isAsync) ? true : isAsync,
                success : function(data, msg, res){
                    $('.wrap-loading').addClass('display-none');
                    if(callback != null){
                        callback(data);
                    } else{
                        return true;
                    }
                },
                error : function(request,error){
                    $('.wrap-loading').addClass('display-none');
                    if(request.status == 0 || request.status == 200){
                        return false;
                    } else if(request.status == 404){
                        if($formCheck.isNullStr(request.responseText)){
                            comShowPopup(1, "", "알 수 없는 요청입니다.");
                        }else{
                            comShowPopup(1, "", JSON.parse(request.responseText).message);
                        }
                    } else if(request.status == 401){
                        if($formCheck.isNullStr(request.responseText)){
                            comShowPopup(1, "", "사용자 권한이 없습니다.");
                        }else{
                            comShowPopup(1, "", JSON.parse(request.responseText).message);
                        }
                    } else if(request.status == 500){
                        if($formCheck.isNullStr(request.responseText)){
                            comShowPopup(1, "", "서버와 통신 중 오류가 발생하였습니다.</br>다시 시도해주십시오.");
                        }else{
                            comShowPopup(1, "", JSON.parse(request.responseText).message);
                        }
                    } else if(error == 'timeout'){
                        if($formCheck.isNullStr(request.responseText)){
                            comShowPopup(1, "", "요청 시간이 초과되었습니다.</br>다시 시도해 주세요");
                        }else{
                            comShowPopup(1, "", JSON.parse(request.responseText).message);
                        }
                    } else{
                        comShowPopup(1, "", "시스템오류가 발생하였습니다.</br>관리자에게 문의해 주십시오.");
                    }

                    $('.wrap-loading').addClass('display-none');
                }
            };
            $.ajax(options);

            // 2. AjaxForm 전송
        }else{
            var AjaxFrm = formObj;

            if(url != null){
                AjaxFrm.attr('action', url);
            }

            if(methodType != undefined || methodType != null){
                AjaxFrm.attr('method', methodType);
            }else{
                AjaxFrm.attr('method','POST');
            }

            if(callback != undefined || callback != null ){
                AjaxFrm.ajaxForm({
                    success : function(data){
                        callback(data);
                    },
                    error : function(request,error){
                        if(request.status == 0 || request.status == 200){
                            return false;
                        } else if(request.status == 404){
                            if($formCheck.isNullStr(request.responseText)){
                                comShowPopup(1, "", "알 수 없는 요청입니다.");
                            }else{
                                comShowPopup(1, "", JSON.parse(request.responseText).message);
                            }
                        } else if(request.status == 401){
                            if($formCheck.isNullStr(request.responseText)){
                                comShowPopup(1, "", "사용자 권한이 없습니다.");
                            }else{
                                comShowPopup(1, "", JSON.parse(request.responseText).message);
                            }
                        } else if(request.status == 500){
                            if($formCheck.isNullStr(request.responseText)){
                                comShowPopup(1, "", "서버와 통신 중 오류가 발생하였습니다.</br>다시 시도해주십시오.");
                            }else{
                                comShowPopup(1, "", JSON.parse(request.responseText).message);
                            }
                        } else if(error == 'timeout'){
                            if($formCheck.isNullStr(request.responseText)){
                                comShowPopup(1, "", "요청 시간이 초과되었습니다.</br>다시 시도해 주세요");
                            }else{
                                comShowPopup(1, "", JSON.parse(request.responseText).message);
                            }
                        } else{
                            comShowPopup(1, "", "시스템오류가 발생하였습니다.</br>관리자에게 문의해 주십시오.");
                        }
                    }
                });
            }
            AjaxFrm.submit();
        }
    }
}

function toGetParam(json){
    var retStr = "";
    $.each(json,function(key,value){
        retStr += key + '=' + value + '&';
    });
    retStr = (retStr != "")? retStr.substr(0, retStr.length - 1) : retStr;

    return retStr;
}

function getCurrentMonth() {
    var cur = new Date();

    var retStr = cur.getFullYear() + '-0' + (cur.getMonth()+1);

    return retStr;

}

function getNowMonth(separator){
    if(separator == undefined) separator = "";

    var now = new Date();
    var yyyy = now.getFullYear();
    var mm = addZero(now.getMonth() + 1);

    return yyyy + separator + mm;

}

function getNowDate(separator){
    if(separator == undefined) separator = "";

    var now = new Date();
    var yyyy = now.getFullYear();
    var mm = addZero(now.getMonth() + 1);
    var dd = addZero(now.getDate());
    return yyyy + separator + mm + separator + dd;

}

function getTwoMonthAgo(date){
    var month = date.replace('-','').substring(4,6);
    var twoMonAgo = parseInt(date.replace('-','')) - 2;
    if(month == '01'){
        twoMonAgo = parseInt(date.substring(0,4)) - 1 + '' + '11';
    }else if(month == '02'){
        twoMonAgo = parseInt(date.substring(0,4)) - 1 + '' + '12';
    }
    return twoMonAgo.toString();
}

function addZero(i){
    if(i < 10){
        i = "0" + i;
    }
    return i;
}

function isNull(v){
    var _v = v || '';
    return _v === '';
}
