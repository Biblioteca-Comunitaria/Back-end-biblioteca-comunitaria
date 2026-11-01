import {useState} from "react";
import logo from "../assets/logo.png";

export default function Login({onLogin,onInfo}){
  const[user,setUser]=useState("");
  const[pass,setPass]=useState("");
  const[err,setErr]=useState("");

  const submit=e=>{
    e.preventDefault();
    if(!user.trim()||!pass)return setErr("Preencha username e senha.");
    setErr("");onLogin();
  };

  return (<div className="login-page">
    <div className="login-brand">
      <img src={logo} alt="Logo Biblioteca Luiz Brasil"/>
      <h2>BIBLIOTECA</h2>
      <span className="script">Luiz Brasil</span>
    </div>

    <form className="login-box" onSubmit={submit} noValidate>
      <h1>LOGIN</h1>
      <p className="welcome">Bem-vindo de volta, bibliotecário!</p>

      <input className="login-input" placeholder="Username" aria-label="Username" autoComplete="username"
        value={user} onChange={e=>setUser(e.target.value)}/>
      <input className="login-input" type="password" placeholder="Senha" aria-label="Senha" autoComplete="current-password"
        value={pass} onChange={e=>setPass(e.target.value)}/>

      {err&&(<div className="login-err" role="alert">{err}</div>)}
      <button className="login-confirm" type="submit">Confirmar</button>

      <button type="button" className="login-link" onClick={()=>onInfo("Recuperação de senha ainda não disponível.")}>Esqueci a senha!</button>
      <p className="login-small">Ainda não possui login? <button type="button" className="login-link" onClick={()=>onInfo("Cadastro ainda não disponível.")}>Cadastra-se</button></p>
    </form>
  </div>);
}
