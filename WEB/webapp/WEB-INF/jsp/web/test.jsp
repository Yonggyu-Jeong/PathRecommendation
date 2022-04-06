<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" pageEncoding="UTF-8" %>
<html>


<head>
	<meta charset="utf-8"/>
	<title>Kakao 지도 시작하기</title>		
	<script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=e6feec61aefd19d699d2b2d52dad5d39"></script>
	<script>
		var container = document.getElementById('map');
		var options = {
			center: new kakao.maps.LatLng(33.450701, 126.570667),
			level: 3
		};
		var map = new kakao.maps.Map(container, options);
	</script>
</head>
<body>
	<div id="map" style="width:500px;height:400px;"></div>
	<div> 작동 중 </div>

</body>
</html>