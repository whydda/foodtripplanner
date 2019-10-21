/**
 * 폼 체크
 */
var $formCheck = {

	/**
	 * 입력값이  null 인지 체크한다
	 */
	isNullStr : function(str) {
		if (str == null || str == "" || str == void(0)) {
			return true;
		} else {
			return false;
		}
	},

	/**
	 * 입력값이  null 인지 체크하고 메세지 뿌려준다
	 */
	isNull : function( obj , msg) {
		if (obj.value == null || obj.value == "") {
			alert(msg);
			obj.focus();
			return true;
		} else {
			return false;
		}
	},

	/**
	 * 입력값이 스페이스 이외의 의미있는 값이 있는지 체크한다
	 * if (isEmpty(form.keyword)){
	 *       alert('값을 입력하여주세요');
	 * }
	 */
	isEmpty : function(str) {
		if (str == null || str.replace(/ /gi, "") == "") {
			return true;
		} else {
			return false;
		}
	},

	/**
	 * 입력값에 특정 문자가 있는지 체크하는 로직이며
	 * 특정문자를 허용하고 싶지 않을때 사용할수도 있다
	 * char : 제외문자
	 * if (containsChars(form.name, "!,*&^%$#@~;")){
	 *       alert("특수문자를 사용할수 없습니다");
	 * }
	 */
	containsChars : function(str, chars) {
		for (var i = 0; i < str.length; i++) {
			if (chars.indexOf(str.charAt(i)) != -1) {
				return true;
			}
		}
		return false;
	},

	/**
	 * 입력값이 특정 문자만으로 되어있는지 체크하며
	 * 특정문자만을 허용하려 할때 사용한다.
	 * chars : 허용문자
	 * if (containsChars(form.name, "ABO")){
	 *    alert("혈액형 필드에는 A,B,O 문자만 사용할수 있습니다.");
	 * }
	 */
	containsCharsOnly : function(str, chars) {
		for (var i = 0; i < str.length; i++) {
			if (chars.indexOf(str.charAt(i)) == -1) {
				return true;
			}
		}
		return false;
	},

	/**
	 * 입력값이 알파벳인지 체크
	 * 아래 isAlphabet() 부터 isNumComma()까지의 메소드가 자주 쓰이는 경우에는
	 * var chars 변수를 global 변수로 선언하고 사용하도록 한다.
	 * var uppercase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	 * var lowercase = "abcdefghijklmnopqrstuvwxyz";
	 * var number = "0123456789";
	 * function isAlphaNum(str){
	 *       var chars = uppercase + lowercase + number;
	 *    return containsCharsOnly(str, chars);
	 * }
	 */
	isAlphabet : function(str) {
		var chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
		return formCheck.containsCharsOnly(str, chars);
	},

	/**
	 * 입력값이 알파벳 대문자인지 체크한다
	 */
	isUpperCase : function(str) {
		var chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		return formCheck.containsCharsOnly(str, chars);
	},

	/**
	 * 입력값이 알파벳 소문자인지 체크한다
	 */
	isLowerCase : function(str) {
		var chars = "abcdefghijklmnopqrstuvwxyz";
		return formCheck.containsCharsOnly(str, chars);
	},

	/**
	 * 입력값이 숫자만 있는지 체크한다.
	 */
	isNumer : function(str) {
		var chars = "0123456789";
		return formCheck.containsCharsOnly(str, chars);
	},

	/**
	 * 입려값이 알파벳, 숫자로 되어있는지 체크한다
	 */
	isAlphaNum : function(str) {
		var chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		return formCheck.containsCharsOnly(str, chars);
	},

	/**
	 * 입력값이 숫자, 대시"-" 로 되어있는지 체크한다 전화번호나 우편번호, 계좌번호에 - 체크할때 유용하다
	 */
	isNumDash : function(str) {

		var chars = "-0123456789";
		return formCheck.containsCharsOnly(str, chars);
	},

	/**
	 * 입력값이 숫자, 콤마',' 로 되어있는지 체크한다
	 */
	isNumComma : function(str) {
		var chars = ",0123456789";
		return formCheck.containsCharsOnly(str, chars);
	},

	/**
	 * 입력값이 사용자가 정의한 포맷 형식인지 체크
	 * 자세한 format 형식은 자바스크립트의 'reqular expression' 참고한다
	 */
	isValidFormat : function(str, format) {
		if (str.search(format) != -1) {
			return true; // 올바른 포멧형식
		}
		return false;
	},


	/**
	 * 시작 날짜가 종료날짜보다 차이계산
	 */
	dateChk: function(date1,date2){
		var v1=date1.split('-');
		var v2=date2.split('-');

		var a1=new Date(v1[0],v1[1],v1[2]).getTime();
		var a2=new Date(v2[0],v2[1],v2[2]).getTime();
		var b=(a2-a1)/(1000*60*60*24);

		return b;//0이하이면 시작날짜가 종료날짜 이후
	},
	/**
	 * 입력값이 이메일 형식인지 체크한다
	 * if (!isValidEmail(form.email)){
	 *       alert("올바른 이메일 주소가 아닙니다");
	 * }
	 */
	isValidEmail : function(str) {
		var format = /^((\w|[\-\.])+)@((\w|[\-\.])+)\.([A-Za-z]+)$/;
		return formCheck.isValidFormat(str, format);
	},

	/**
	 * 입력값이 URL 형식인지 체크한다
	 */
	isValidUrl : function(str) {
		var format = /^(?:[\w\-\/]{2,}\.)+[a-zA-Z]{2,}$/;
		return formCheck.isValidFormat(str, format);
	},

	/**
	 * 입력값이 전화번호 형식(숫자-숫자-숫자)인지 체크한다
	 */
	isValidPhone : function(str) {
		var format = /^[0-9]{2,3}-?[0-9]{3,4}-?[0-9]{4}$/;
		return formCheck.isValidFormat(str, format);
	},

	/**
	 * 입력값이 날짜 형식(0000-00-00)인지 체크한다(1970-01-01부터 2099-12-31까지 검색 가능)
	 * 정상 format일 경우 true, 잘못된 format일 경우 false
	 */
	isDateFormat: function(str) {
	    var format = /^(19[7-9][0-9]|20\d{2})-(0[0-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])$/;
	    return formCheck.isValidFormat(str, format);
	},
	/**
	 * 입력값의 바이트 길이를 리턴한다.
	 * if (getByteLength(form.title) > 100){
	 *    alert("제목은 한글 50자 (영문 100자) 이상 입력할수 없습니다");
	 * }
	 */
	getByteLength : function(str) {
		var byteLength = 0;
		for (var inx = 0; inx < str.length; inx++) {
			var oneChar = escape(str.charAt(inx));
			if (oneChar.length == 1) {
				byteLength++;
			} else if (oneChar.indexOf("%u") != -1) {
				byteLength += 2;
			} else if (oneChar.indexOf("%") != -1) {
				byteLength += oneChar.length / 3;
			}
		}
		return byteLength;
	},

	/**
	 * 입력값에서 콤마를 없앤다
	 */
	removeComma : function(str) {
		return str.replace(/,/gi, "");
	},

	/**
	 * 선택된 라디오버튼이 있는지 체크한다
	 */
	hasCheckedRadio : function(obj) {
		if (obj.length > 1) {
			for (var inx = 0; inx < obj.length; inx++) {
				if (obj[inx].checked)
					return true;
			}
		} else if (obj.length == 1) {
			if (obj[0].checked)
				return true;
		} else {
			if (obj.checked)
				return true;
		}
		return false;
	},

	/**
	 * 선택된 체크박스가 있는지 체크
	 */
	hasCheckedBox : function(obj) {
		return formCheck.hasCheckedRadio(obj);
	},

	/**
	 * 숫자 천단위 마다 콤마 찍는다.
	 */
	numberFormat : function(obj) {
		if(obj == 0) return 0;

		var reg = /(^[+-]?\d+)(\d{3})/;
		var n = (obj + '');

		while (reg.test(n)) n = n.replace(reg, '$1' + ',' + '$2');
		return n;
	},

	/**
	 * 숫자만 입력 가능하도록
	 * 숫자 외의 문자는 공백처리 후 3자리마다 콤마 붙임
	 */
	onlyNumber : function(value){
		return formCheck.numberFormat(Number(value.replace(/[^0-9]/gi,"")));
	},

	/**
	 * 입력값이 주민등록번호 형식(숫자-숫자)인지 체크한다
	 */
	isValidJuminNumber : function(str) {
		var format = /^(?:[0-9]{2}(?:0[1-9]|1[0-2])(?:0[1-9]|[1,2][0-9]|3[0,1]))-[1-4][0-9]{6}$/;
		return formCheck.isValidFormat(str, format);
	},

	/**
	 * 숫자만 남기고 공백으로 replace한다.
	 */
	removeChar : function(str) {
		return str.replace(/[^0-9]/gi,"");
	},

	/**
	 * 숫자앞에 0으로 채운다.
	 */
	leadingZeros : function(n, digits) {
	  var zero = '';
	  n = n.toString();

	  if (n.length < digits) {
	    for (var i = 0; i < digits - n.length; i++)
	      zero += '0';
	  }
	  return zero + n;
	},

	/**
	 * max count 체크(20)
	 */
	ckMaxCnt : function(n){
		var flag = n > 20? false : true;
		return flag;
	},

	/**
	 * single file size 체크 (max 100MB --- boolean)
	 */
	ckSingleFileSize : function(file){
		var size = file[0].size;
		var flag = size >= 104857600? false : true;
		return flag;
	},

	/**
	 * multi file size 체크 (max 100MB --- boolean)
	 */
	ckMultiFileSize : function(arrFiles){
		var size = 0;
		$(arrFiles).each(function(a, b){
			var sSize = arrFiles[a].size;
			size = size + sSize;
		});
		var flag = size >= 104857600? false : true;
		return flag;
	},

	/**
	 * get single file size
	 */
	getSingleFileSize : function(file){
		var size = file[0].size;
		return size;
	},

	/**
	 * get multi file size
	 */
	getMultiFileSize : function(arrFiles){
		var size = 0;

		$(arrFiles).each(function(a, b){
			var sSize = arrFiles[a].size;
			size = size + sSize;
		});
		return size;
	},

	//xss 처리
	XSSfilter : function( content ) {
	    return content.replace(/&/g, '&')
	      .replace(/</g, '<')
	      .replace(/>/g, '>')
	      .replace(/\"/g, '"')
	      .replace(/\'/g, '&#39;')
	      .replace(/\//g, '&#x2F;');
	},

	//패스워드 체크
	checkUserPassWd : function(str, str2){
		var regExpPasswd = /^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-]|.*[0-9]).{6,16}$/; //6~16자의 영문 대소문자, 숫자, 특수기호를 혼합 된 형태만 가능
		if(str == ''){
			alert("비밀번호를 입력하세요.");
			return false;
		}else if(!regExpPasswd.test(str)){
			alert("비밀번호는 6~16자의 영문 대소문자, 숫자, 특수기호를 혼합 된 형태만 가능 합니다.");
			return false;
		}else if(typeof str2 != "undifined" &&  str != str2){
			alert('비밀번호가 일치하지 않습니다.');
			return false;
		}
		return true;
	},

	//아이디 체크
	checkUserId : function(str){
		var regExpId = /^[a-z0-9]{5,20}$/g;

		if(str == ""){
			alert("ID가 존재하지 않습니다.")
			return false;
		}
		if(!regExpId.test(str)){
			alert(""); //5~20자의 영문 소문자 숫자만 입력이 가능합니다.
			return false;
		}

		return true;
	},

	//자바스크립트에 들어가는 문자열 치환
	fnEscapeJavascript : function(str){
		return str.split('\'').join('\\\'').split('\"').join('\\\"');
	},

	//이미지체크
	checkExtFile : function(filename) {
		if(filename != null && filename != "") {
			dot = filename.lastIndexOf(".");
			ext = filename.substring(dot).toLowerCase();

			if (ext == ".png" || ext == ".jpg" || ext == ".bmp"  || ext == ".jpeg" || ext == ".gif") {
				return true;
			} else {
				return false;
			}
		}
	},

	//다중 파일 확장자 체크(이미지)
	checkMultiExtFile : function(fileList) {
		var ckImg = true;

		$(fileList).each(function(a,b){
			ckImg = checkExtFile($(this)[0].name);
			if(!ckImg){
				return false;
			}
		});
		return ckImg;
	}

};
