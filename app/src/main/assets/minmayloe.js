/*
        xGetter
          By
    Khun Htetz Naing [fb.com/KHtetzNaing]
Repo => https://github.com/KhunHtetzNaing/xGetter

*/

var openload = /https?:\/\/(www\.)?(openload|oload)\.[^\/,^\.]{2,}\/(embed|f)\/.+/i,
    stream = /https?:\/\/(www\.)?(streamango|fruitstreams|streamcherry|fruitadblock|fruithosts)\.[^\/,^\.]{2,}\/(f|embed)\/.+/i,
    megaup = /https?:\/\/(www\.)?(megaup)\.[^\/,^\.]{2,}\/.+/i,
    mp4upload = /https?:\/\/(www\.)?mp4upload\.[^\/,^\.]{2,}\/embed\-.+/i,
    sendvid = /https?:\/\/(www\.)?(sendvid)\.[^\/,^\.]{2,}\/.+/i,
    vidcloud = /https?:\/\/(www\.)?(vidcloud|vcstream|loadvid)\.[^\/,^\.]{2,}\/embed\/([a-zA-Z0-9]*)/i,
    rapidvideo = /https?:\/\/(www\.)?rapidvideo\.[^\/,^\.]{2,}\/(\?v=[^&\?]*|e\/.+|v\/.+)/i;

if (openload.test(window.location.href)) {
    xGetter.fuck(document.location.protocol + '//' + document.location.host + '/stream/' + document.getElementById("DtsBlkVFQx").textContent + '?mime=true');
} else if (stream.test(window.location.href)) {
    xGetter.fuck(window.location.protocol + srces[0]["src"]);
} else if (megaup.test(window.location.href)) {
    seconds = 0;
    display();
    window.location.replace(document.getElementsByClassName("btn btn-default").item(0).href);
} else if (mp4upload.test(window.location.href)) {
    xGetter.fuck(document.getElementsByClassName('jw-video jw-reset').item(0).src);
} else if (rapidvideo.test(window.location.href)) {
    xGetter.fuck(document.getElementsByTagName('source').item(0).src);
} else if (sendvid.test(window.location.href)) {
    xGetter.fuck(document.getElementsByTagName('source').item(0).src);
} else if (vidcloud.test(window.location.href)) {
    $.ajax({
        url: '/download',
        method: 'POST',
        data: {
            file_id: fileID
        },
        dataType: 'json',
        success: function(res) {
            $('.quality-menu').html(res.html);
            var data = res.html;
            var regex = /href="(.*?)"/;
            var m;
            if ((m = regex.exec(data)) !== null) {
                xGetter.fuck(m[1]);
            }
        }
    });
}

/*
Supported Sites
=> Openload (All domains)
=> FruitStreams (Streamcherry,Streamango and etc..)
=> Mp4Upload
=> RapidVideo
=> SendVid
=> MegaUp
=> VidCloud (All domains)
*/