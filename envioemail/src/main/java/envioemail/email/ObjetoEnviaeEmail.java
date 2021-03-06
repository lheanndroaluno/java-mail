package envioemail.email;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

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
	
	
	public void enviarEmailAnexo(boolean envioHtml) throws Exception {
		
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
		
		
		/*Parte 1 do e-mail que é o texto e a descrição do e-mail*/
		MimeBodyPart corpoEmail = new MimeBodyPart();
		
		
		if (envioHtml) {
			corpoEmail.setContent(textoEmail, "text/html; chatset=UTF-8");
		} else {
			corpoEmail.setText(textoEmail);
		}
		
		/*enviando uma lista de Pdf's*/
		List<FileInputStream> arquivos = new ArrayList<FileInputStream>();
		arquivos.add(simuladorDePdf());/*Poderia um certificado*/
		arquivos.add(simuladorDePdf());/*Poderia ser um documento*/
		arquivos.add(simuladorDePdf());/*Poderia ser uma nota fiscal*/
		arquivos.add(simuladorDePdf());/*Poderia ser uma imagem*/
		
		
		/*Parte 3 Junção do corpo do email com o anexo do email*/
		Multipart multipart = new MimeMultipart();
		multipart.addBodyPart(corpoEmail);
		
		int index = 1;
		for (FileInputStream fileInputStream : arquivos) {
			
			/*Parte 2 do e-mail, que são os anexos em pdf*/
			MimeBodyPart anexoEmail = new MimeBodyPart();
			
			/*Onde é passado o simuladorDePdf, você passa o seu arquivo gravado no banco de dados*/
			anexoEmail.setDataHandler(new DataHandler(new ByteArrayDataSource(fileInputStream, "application/pdf")));
			anexoEmail.setFileName("arquivoAnexo"+index+".pdf");
			
			
			multipart.addBodyPart(anexoEmail);
			index++;//realizar o incremento do index do arquivo
		}
		
		
		message.setContent(multipart);
		
		Transport.send(message);
		
	}
	
	
	/**
	 * Esse método simula o pdf ou qualquer arquivo que possa ser enviado por anexo no e-mail.
	 * Você pode pegar o arquivo no seu banco de dados base64, byte[], Stream de arquivos, 
	 * pode estar em um banco de dados, ou em uma pasta.
	 * 
	 * @return um PDF em branco com o texto do parágrafo de exemplo
	 * @throws Exception
	 */
	private FileInputStream simuladorDePdf() throws Exception{
		
		Document documento = new Document();
		File file = new File("arquivoAnexo.pdf");
		if (!file.exists()) {
			file.createNewFile();
		}
		PdfWriter.getInstance(documento, new FileOutputStream(file));
		documento.open();
		documento.add(new Paragraph("Conteúdo do PDF anexo com Java Mail, esse texto é do PDF"));
		documento.close();
		
		
		return new FileInputStream(file);//retorna esse PDF
	}
}
