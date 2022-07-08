package envioemail.email;

import java.util.Date;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class ObjetoEnviaeEmail {

	private String userName = "leandroamorimst@gmail.com";
	private String userPassword = "isnwccicumbjmvxg";
	
	private String listaDestinatarios = "";/*Inicia como vazio*/
	private String nomeRemetente = "";/*Nome de quem estar enviando o email*/
	private String assuntoEmail = "";/*Título do email*/
	private String textoEmail = "";/*Texto do email*/
	
	/**
	 * Construtor da classe
	 * @param listaDestinatarios
	 * @param nomeRemetente
	 * @param assuntoEmail
	 * @param textoEmail
	 */
	public ObjetoEnviaeEmail(String listaDestinatarios, String nomeRemetente, String assuntoEmail, String textoEmail) {
		this.listaDestinatarios = listaDestinatarios;
		this.nomeRemetente = nomeRemetente;
		this.assuntoEmail = assuntoEmail;
		this.textoEmail = textoEmail;
	}
	
	public void enviarEmail(boolean envioHtml) throws Exception {
		
		/**
		 * Configuração das propriedades
		 */
		Properties properties = new Properties();
				 //put(chave, valor)
		properties.put("mail.smtp.ssl.trust", "*");
		properties.put("mail.smtp.auth", "true");/*Autorização*/
		properties.put("mail.smtp.starttls.enable", "true");/*Autenticação*/
		properties.put("mail.smtp.host", "smtp.gmail.com");/*Servidor do gamil - Google*/
		properties.put("mail.smtp.port", "587");/*Porta smtp do servidor*/
		properties.put("mail.smtp.socketFactory.port", "465");/*Especifica a porta a ser utilizada para conexão pelo socket*/
		properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");/*Classe Socket de conexão ao SMTP*/
		
		
		Session session = Session.getInstance(properties, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(userName, userPassword); 
			}
		});
		
		/*Estabelecemos para quem iremos enviar*/
		Address[] toUser = 
				InternetAddress.parse(listaDestinatarios);
		
		
		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(userName, nomeRemetente));/*Quem está enviando*/
		message.setRecipients(Message.RecipientType.TO, toUser);/*Email de destino, pode ser um email, ou uma lista de emails*/
		message.setSubject(assuntoEmail);
		message.setSentDate(new Date());
		if (envioHtml) {
			message.setContent(textoEmail, "text/html; chatset=UTF-8");
		} else {
			message.setText(textoEmail);
		}
		
		
		Transport.send(message);
		
	}
}
