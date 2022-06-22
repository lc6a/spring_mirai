
function host() {
    //return "49.235.33.223:13000"
    //return "localhost:13000"
    return window.location.host
}
    axios.defaults.baseURL = "http://" + host();
    axios.defaults.headers['Content-Type'] = 'application/x-www-form-urlencoded';
    //axios.defaults.params = {token: localStorage.getItem("token")}
    axios.defaults.error

    // 添加请求拦截器
    axios.interceptors.request.use(function (config) {
        // 在发送请求之前做些什么
        if (config.url.indexOf("?") !== -1) {
            config.url += "&token=" + localStorage.getItem("token")
        } else {
            config.url += "?token=" + localStorage.getItem("token")
        }
        return config;
    }, function (error) {
        // 对请求错误做些什么
        alert(error);
        return Promise.reject(error);
    });

    // 添加响应拦截器
    axios.interceptors.response.use(function (response) {
        // 对响应数据做点什么
        if (JSON.stringify(response.data) === JSON.stringify({ ok: false, msg: "unLogin" })) {
            debugger;
            alert("请先登录")
            window.location = "/login.html"
        }
        if (typeof (response.data) === "string") {
            response.data = JSON.parse(response.data)
            console.log(response);
        }
        return response;
    }, function (error) {
        // 对响应错误做点什么
        return Promise.reject(error);
    });
