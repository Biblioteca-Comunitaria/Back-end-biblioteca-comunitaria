import {useState,useEffect} from "react";
import {KEY,SESSION,DAY,iso,fmt,digits,seed,load,getRoute} from "./utils";
import Icon from "./components/Icon";
import Login from "./components/Login";
import logo from "./assets/logo.png";
import {Books,BookDetail,AddBook} from "./components/Books";
import {Loans,LoanDetail} from "./components/Loans";

export default function App(){
  const[db,setDb]=useState(load);
  const[route,setRoute]=useState(getRoute);
  const[toast,setToast]=useState(null);
  const[sel,setSel]=useState(null);       // livro selecionado
  const[selLoan,setSelLoan]=useState(null);
  const[adding,setAdding]=useState(false);
  const[q,setQ]=useState("");
  const[tab,setTab]=useState("todos");
  const[session,setSession]=useState(()=>{try{return localStorage.getItem(SESSION)}catch(e){return null}});

  useEffect(()=>{try{localStorage.setItem(KEY,JSON.stringify(db))}catch(e){}},[db]);
  useEffect(()=>{const h=()=>{setRoute(getRoute());setSel(null);setSelLoan(null);setAdding(false);setQ("");setTab("todos")};addEventListener("hashchange",h);return()=>removeEventListener("hashchange",h)},[]);
  const say=(msg,bad)=>{setToast({msg,bad});setTimeout(()=>setToast(null),3500)};
  const today=iso(Date.now());

  const book=id=>db.books.find(b=>b.id===id);
  const reader=id=>db.readers.find(r=>r.id===id);
  const activeOf=bid=>db.loans.filter(l=>l.bookId===bid&&!l.returned).length;
  const avail=b=>b.copies-activeOf(b.id);
  const status=l=>l.returned?"Devolvido":(l.due<today?"Atrasado":"Ativo");

  // RF1
  function addBook(f){
    const title=f.title.trim(),author=f.author.trim(),genre=f.genre.trim(),publisher=f.publisher.trim(),isbn=f.isbn.trim(),copies=Number(f.copies),year=Number(f.year),status=f.status;
    if(!title||!author||!genre||!publisher||!isbn)return say("Preencha todos os campos.",1);
    if(!Number.isInteger(year)||year<1000||year>new Date().getFullYear()+1)return say("Informe um ano de publicação válido.",1);
    if(!Number.isInteger(copies)||copies<1)return say("Exemplares disponíveis deve ser ao menos 1.",1);
    if(db.books.some(b=>b.isbn.replace(/\D/g,"")===isbn.replace(/\D/g,"")))return say("Já existe um livro com esse ISBN.",1);
    const nb={id:Date.now(),title,author,genre,publisher,year,isbn,copies,status};
    setDb({...db,books:[...db.books,nb]});setAdding(false);say("Livro cadastrado!");return true;
  }
  // RF3
  function lend(bookId,cpf){
    const r=db.readers.find(x=>digits(x.cpf)===digits(cpf));
    const b=book(bookId);
    if(!r)return say("Leitor não encontrado para este CPF.",1);
    if(b.status==="Indisponível")return say("Este livro está marcado como indisponível.",1);
    if(avail(b)<1)return say("Não há exemplares disponíveis.",1);
    if(db.loans.filter(l=>l.readerId===r.id&&!l.returned).length>=2)return say(r.name+" já tem 2 empréstimos ativos.",1);
    const due=iso(Date.now()+14*DAY);
    setDb({...db,loans:[...db.loans,{id:Date.now(),bookId,readerId:r.id,date:today,due,returned:null}]});
    say("Empréstimo feito para "+r.name+". Devolução até "+fmt(due)+".");return true;
  }
  // RF4
  function giveBack(id){
    setDb({...db,loans:db.loans.map(l=>l.id===id?{...l,returned:today}:l)});
    setSelLoan(null);say("Devolução registrada em "+fmt(today)+".");
  }

  const go=r=>{location.hash=r};
  const login=()=>{try{localStorage.setItem(SESSION,"1")}catch(e){}setSession("1");go("livros")};
  const logout=()=>{try{localStorage.removeItem(SESSION)}catch(e){}setSession(null);go("login")};
  const titles={inicio:"Início",livros:"Livros",emprestimos:"Empréstimo",config:"Configurações"};
  const showSearch=(route==="livros"&&!sel&&!adding)||(route==="emprestimos"&&!selLoan);

  if(!session)return (<>
    <Login onLogin={login} onInfo={say}/>
    {toast&&(<div className={"toast"+(toast.bad?" bad":"")} role="status">{toast.msg}</div>)}
  </>);

  return (<div className="app">
    <nav aria-label="Menu">
      {["livros","emprestimos"].map(n=>(<button key={n} className={route===n?"on":""} onClick={()=>go(n)} aria-label={titles[n]} title={titles[n]}><Icon n={n}/></button>))}
    </nav>
    <main>
      <header>
        <div className="logo"><img src={logo} alt="Logo Biblioteca Luiz Brasil" className="logo-img"/><div>BIBLIOTECA<small>Luiz Brasil</small></div></div>
        <div className="hi"><span>Olá, bibliotecário!</span><div className="av">👤</div><button className="btn alt sm" onClick={logout}>Sair</button></div>
      </header>
      <div className="titlebar">
        <h1>{adding?"Cadastro de novo livro":titles[route]}</h1>
        {showSearch&&(<input className="search" placeholder={route==="livros"?"Buscar por título ou autor...":"Buscar por livro ou leitor..."} value={q} onInput={e=>setQ(e.target.value)} aria-label="Buscar"/>)}
        {route==="livros"&&!sel&&!adding&&(<button className="btn" onClick={()=>setAdding(true)}>+ Novo livro</button>)}
      </div>

      {route==="livros"&&(adding?(<AddBook onSave={addBook} onCancel={()=>setAdding(false)}/>):
        sel?(<BookDetail b={book(sel)} avail={book(sel).status==="Indisponível"?0:avail(book(sel))} loans={db.loans.filter(l=>l.bookId===sel&&!l.returned)} reader={reader} readers={db.readers} onLend={lend} back={()=>setSel(null)} status={status}/>):
        (<Books db={db} q={q} tab={tab} setTab={setTab} avail={avail} open={setSel}/>))}
      {route==="emprestimos"&&(selLoan?(<LoanDetail l={db.loans.find(l=>l.id===selLoan)} book={book} reader={reader} status={status} onReturn={giveBack} back={()=>setSelLoan(null)}/>):
        (<Loans db={db} q={q} tab={tab} setTab={setTab} status={status} book={book} reader={reader} open={setSelLoan}/>))}
      {route==="config"&&(<div className="panel" style={{marginTop:20}}><h2 style={{fontSize:20}}>Dados</h2><p>Os dados ficam salvos neste navegador. Restaure os dados iniciais (livros, leitores fictícios e empréstimos de exemplo) quando quiser.</p><button className="btn" onClick={()=>{if(confirm("Restaurar os dados iniciais?")){setDb(seed());say("Dados iniciais restaurados.")}}}>Restaurar dados iniciais</button></div>)}
    </main>
    {toast&&(<div className={"toast"+(toast.bad?" bad":"")} role="status">{toast.msg}</div>)}
  </div>);
}
