import {useState} from "react";
import {norm,digits,fmt} from "../utils";

export function Books({db,q,tab,setTab,avail,open}){
  const list=db.books.filter(b=>{
    const m=!q||norm(b.title).includes(norm(q))||norm(b.author).includes(norm(q));
    const a=avail(b);
    const a2=b.status==="Indisponível"?0:a;return m&&(tab==="todos"||(tab==="disp"&&a2>0)||(tab==="indisp"&&a2<1));
  });
  return (<div>
    <div className="tabs">{[["todos","Todos"],["disp","Disponíveis"],["indisp","Indisponíveis"]].map(([k,t])=>(<button key={k} className={tab===k?"on":""} onClick={()=>setTab(k)}>{t}</button>))}</div>
    {list.length===0?(<div className="empty">Nenhum livro encontrado.</div>):
    (<div className="grid">{list.map(b=>{const a=b.status==="Indisponível"?0:avail(b);return (<button className="card bk" key={b.id} onClick={()=>open(b.id)}>
      <div className="cover"></div><div className="bt">{b.title}</div><div>{b.author}</div>
      <div className={"sm "+(a>0?"ok":"no")}>{a>0?"Disponível":"Indisponível"}: {a} de {b.copies}</div></button>)})}</div>)}
  </div>);
}

export function BookDetail({b,avail,loans,reader,readers,onLend,back,status}){
  const[cpf,setCpf]=useState("");
  const found=cpf&&readers.find(r=>digits(r.cpf)===digits(cpf));
  return (<div className="two">
    <div>
      <div style={{display:"flex",gap:20,alignItems:"center",flexWrap:"wrap"}}>
        <div className="cover big"></div>
        <div><h2 style={{fontSize:26}}>{b.title}</h2><div style={{fontSize:18}}>{b.author}</div></div>
      </div>
      <h3 style={{marginTop:30}}>Realizar empréstimo</h3>
      <label for="cpf">CPF do leitor</label>
      <input id="cpf" className="f" placeholder="CPF do usuário (ex.: 111.222.333-44)" value={cpf} onInput={e=>setCpf(e.target.value)} list="cpfs"/>
      <datalist id="cpfs">{readers.map(r=>(<option key={r.id} value={r.cpf}>{r.name}</option>))}</datalist>
      {cpf&&(<div className="sm">{found?"Leitor: "+found.name:"CPF não cadastrado"}</div>)}
      <div className="row">
        <button className="btn g" disabled={avail<1} style={{opacity:avail<1?.5:1}} onClick={()=>{if(onLend(b.id,cpf))setCpf("")}}>Salvar</button>
        <button className="btn alt" onClick={back}>Voltar</button>
      </div>
      {avail<1&&(<div className="no" style={{marginTop:8}}>Sem exemplares disponíveis.</div>)}
      <div className="sm" style={{marginTop:8}}>Prazo: 14 dias a partir de hoje. Máximo de 2 empréstimos ativos por leitor.</div>
    </div>
    <div>
      <div className="panel">
        <h3 style={{margin:"0 0 10px"}}>INFORMAÇÕES ADICIONAIS</h3>
        <p>Total de exemplares – {b.copies}</p>
        <p>Exemplares disponíveis – {avail}</p>
        <p>Status – {avail>0?"Disponível":"Indisponível"}</p>
        <p>Gênero: {b.genre||"—"}</p>
        <p>Editora: {b.publisher||"—"}</p>
        <p>Ano de publicação: {b.year||"—"}</p>
        <p>ISBN: {b.isbn}</p>
      </div>
      <h3>Empréstimos em andamento</h3>
      {loans.length===0?(<div className="sm">Nenhum.</div>):loans.map(l=>(<p key={l.id}>👤 {reader(l.readerId).name} — devolver até {fmt(l.due)} {status(l)==="Atrasado"&&(<span className="warn">◆ Atrasado</span>)}</p>))}
    </div>
  </div>);
}

export function AddBook({onSave,onCancel}){
  const[f,setF]=useState({title:"",author:"",genre:"",publisher:"",year:"",isbn:"",copies:1,status:"Disponível"});
  const set=k=>e=>setF({...f,[k]:e.target.value});
  return (<div className="panel" style={{maxWidth:520,margin:"24px auto"}}>
    <h3 style={{margin:0}}>ADICIONE AS INFORMAÇÕES</h3>
    <label>Título<input className="f" value={f.title} onInput={set("title")}/></label>
    <label>Nome do autor(a)<input className="f" value={f.author} onInput={set("author")}/></label>
    <label>Gênero<input className="f" value={f.genre} onInput={set("genre")}/></label>
    <label>Editora<input className="f" value={f.publisher} onInput={set("publisher")}/></label>
    <label>Ano de publicação<input className="f" type="number" min="1000" value={f.year} onInput={set("year")}/></label>
    <label>ISBN (único)<input className="f" value={f.isbn} onInput={set("isbn")}/></label>
    <label>Exemplares disponíveis<input className="f" type="number" min="1" value={f.copies} onInput={set("copies")}/></label>
    <label>Status<select className="f" value={f.status} onChange={set("status")}><option>Disponível</option><option>Indisponível</option></select></label>
    <div className="row"><button className="btn" onClick={()=>onSave(f)}>Cadastrar novo livro</button><button className="btn alt" onClick={onCancel}>Cancelar</button></div>
  </div>);
}
