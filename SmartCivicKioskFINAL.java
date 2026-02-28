import com.sun.net.httpserver.HttpServer;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class SmartCivicKioskFINAL {

    public static void main(String[] args) throws Exception {

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        server.createContext("/", exchange -> {

            String html = """
<!DOCTYPE html>
<html>
<head>
<meta charset='UTF-8'>
<title>Smart Civic Kiosk</title>

<style>

body{
font-family:Arial;
margin:0;
background:#eef2f7;
}

.page{
display:none;
min-height:100vh;
padding:30px;
}

#login{display:block;}

.container{
max-width:1000px;
margin:auto;
background:white;
padding:25px;
border-radius:12px;
box-shadow:0 0 10px #bbb;
}

.services{
display:grid;
grid-template-columns:repeat(2,1fr);
gap:20px;
margin-top:20px;
}

.card{
border:1px solid #ddd;
border-radius:10px;
padding:10px;
cursor:pointer;
text-align:center;
transition:0.2s;
}

.card:hover{
background:#f1f7ff;
}

.card img{
width:120px;
height:120px;
object-fit:cover;
}

button{
padding:10px 18px;
background:#2d89ef;
color:white;
border:none;
border-radius:6px;
margin:6px;
cursor:pointer;
}

input,textarea,select{
width:90%;
padding:8px;
margin:8px;
}

.status{
padding:6px 12px;
background:green;
color:white;
border-radius:5px;
}

.adminBox{
margin-top:30px;
background:#fff3cd;
padding:15px;
border-radius:10px;
}

</style>


<script>

let otp;
let complaintLevel = "Department";
let solvedBy = "Pending";
let currentService = "";

let complaints = [];

function show(page){

['login','service','complaint','receipt','admin'].forEach(p=>{
document.getElementById(p).style.display='none';
});

document.getElementById(page).style.display='block';

}

function generateOTP(){

let a=document.getElementById('aadhaar').value;

if(a.length!=12){
alert("Enter valid Aadhaar");
return;
}

otp=Math.floor(1000+Math.random()*9000);

alert("Aadhaar Verified");

document.getElementById('otpDisplay').innerHTML="OTP (Demo): "+otp;

}

function login(){

let o=document.getElementById('otp').value;

if(o==otp){

show('service');

}else{

alert("Wrong OTP");

}

}

function chooseService(s){

currentService=s;

document.getElementById('serviceName').innerText=s;

show('complaint');

}

function submitComplaint(){

complaintLevel="Department";
solvedBy="Pending";

let obj={
service:currentService,
status:"Pending",
level:"Department",
solvedBy:"Pending"
};

complaints.push(obj);

document.getElementById('rService').innerText=currentService;
document.getElementById('rStatus').innerText="Pending at Department";
document.getElementById('rSolved').innerText="Not Solved";

show('receipt');

loadAdmin();

}

function escalate(){

if(complaintLevel=="Department"){
complaintLevel="MLA";
}

else if(complaintLevel=="MLA"){
complaintLevel="CM";
}

else if(complaintLevel=="CM"){
complaintLevel="President";
}

document.getElementById('rStatus').innerText="Escalated to "+complaintLevel;

}

function solve(role){

solvedBy=role;

document.getElementById('rStatus').innerText="Solved";
document.getElementById('rSolved').innerText=role;

complaints[complaints.length-1].status="Solved";
complaints[complaints.length-1].solvedBy=role;

loadAdmin();

}

function downloadReceipt(){

window.print();

}

function adminLogin(){

show('admin');

loadAdmin();

}

function loadAdmin(){

let list=document.getElementById('complaints');

list.innerHTML="";

complaints.forEach(c=>{

let row=document.createElement("div");

row.innerHTML=
"Service: "+c.service+
" | Status: "+c.status+
" | Solved By: "+c.solvedBy;

list.appendChild(row);

});

}

</script>
</head>


<body>


<div id="login" class="page">

<div class="container">

<h2>Citizen Login</h2>

<input id="aadhaar" placeholder="Enter Aadhaar Number">

<br>

<button onclick="generateOTP()">Verify Aadhaar + Generate OTP</button>

<div id="otpDisplay"></div>

<input id="otp" placeholder="Enter OTP">

<br>

<button onclick="login()">Login</button>


<div class="adminBox">

<h3>Admin Access</h3>

<button onclick="adminLogin()">Open Admin Dashboard</button>

</div>

</div>

</div>



<div id="service" class="page">

<div class="container">

<h2>Select Service</h2>

<div class="services">

<div class="card" onclick="chooseService('Electricity')">
<img src="https://cdn-icons-png.flaticon.com/512/3075/3075977.png">
<h3>Electricity Service</h3>
</div>

<div class="card" onclick="chooseService('Water')">
<img src="https://cdn-icons-png.flaticon.com/512/728/728093.png">
<h3>Water Supply</h3>
</div>

<div class="card" onclick="chooseService('Gas')">
<img src="https://cdn-icons-png.flaticon.com/512/2917/2917995.png">
<h3>Gas Service</h3>
</div>

<div class="card" onclick="chooseService('Waste')">
<img src="https://cdn-icons-png.flaticon.com/512/484/484662.png">
<h3>Waste Management</h3>
</div>

</div>

</div>

</div>



<div id="complaint" class="page">

<div class="container">

<h2>Complaint - <span id="serviceName"></span></h2>

<textarea placeholder="Describe issue"></textarea>

<br>

<button onclick="submitComplaint()">Submit Complaint</button>

</div>

</div>



<div id="receipt" class="page">

<div class="container">

<h2>Complaint Receipt</h2>

<p>Service : <b id="rService"></b></p>

<p>Status : <span class="status" id="rStatus"></span></p>

<p>Solved By : <b id="rSolved"></b></p>

<br>

<button onclick="escalate()">Escalate</button>

<button onclick="solve('MLA')">Solved by MLA</button>

<button onclick="solve('CM')">Solved by CM</button>

<button onclick="solve('President')">Solved by President</button>

<br>

<button onclick="downloadReceipt()">Download Receipt</button>

<br><br>

<button onclick="show('login')">Home</button>

</div>

</div>



<div id="admin" class="page">

<div class="container">

<h2>Admin Dashboard</h2>

<div id="complaints"></div>

<br>

<button onclick="show('login')">Logout</button>

</div>

</div>


</body>

</html>
""";

            exchange.sendResponseHeaders(200, html.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(html.getBytes());
            os.close();
        });

        server.start();
        System.out.println("Server running http://localhost:8080");
    }
}