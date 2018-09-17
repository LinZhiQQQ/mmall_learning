<%@ page contentType="text/html; charset=utf-8" language="java"
         pageEncoding="utf-8" %>
<html>
<head>
    <meta charset="UTF-8">
</head>
<body>
<h2>Hello World!</h2>

spring mvc 上传文件

<form name="form1" action="/manage/product/upload.do" method="post" enctype="multipart/form-data">

    <input type="file" name="upload_file"/>
    <input type="submit" value="上传文件1"/>
</form>

富文本图片上传
<form name="form2" action="/manage/product/richtext_img_upload.do" method="post" enctype="multipart/form-data">

    <input type="file" name="upload_file"/>
    <input type="submit" value="上传文件2"/>
</form>

</body>
</html>
