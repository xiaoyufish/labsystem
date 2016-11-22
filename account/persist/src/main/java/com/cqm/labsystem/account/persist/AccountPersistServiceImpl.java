package com.cqm.labsystem.account.persist;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.*;
import java.util.List;

/**
 * Created by qmcheng on 2016/11/21 0021.
 */
public class AccountPersistServiceImpl implements AccountPersistService {

    public static final String ELEMENT_ROOT = "account-persist";
    public static final String ELEMENT_ACCOUNTS = "accounts";

    private String file;
    private SAXReader reader = new SAXReader();

    private Document readDocument() throws AccountPersistException {
        File dataFile = new File(getFile());
        if (!dataFile.exists()) {
            dataFile.getParentFile().mkdirs();
            Document doc = DocumentFactory.getInstance().createDocument();
            Element rootElement = doc.addElement(ELEMENT_ROOT);
            rootElement.addElement(ELEMENT_ACCOUNTS);
            writeDocument(doc);
        }
        try {
            return reader.read(new File(getFile()));
        } catch (DocumentException e) {
            throw new AccountPersistException(e);
        }
    }

    private void writeDocument(Document document) throws AccountPersistException {
        Writer out = null;
        try {
            out = new OutputStreamWriter(new FileOutputStream(getFile()));
            XMLWriter writer = new XMLWriter(out, OutputFormat.createPrettyPrint());
            writer.write(document);
        } catch (IOException e) {
            throw new AccountPersistException(e);
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                throw new AccountPersistException(e);
            }
        }
    }

    public Account createAccount() throws AccountPersistException {
        return null;
    }

    public Account readAccount(String id) throws AccountPersistException {
        Document document = readDocument();
        Element accountsElement = document.getRootElement().element(ELEMENT_ACCOUNTS);
        for (Element accountElement : (List<Element>) accountsElement.elements()) {
            if (accountElement.elementText("id").equals(id)) {
                return buildAccount(accountElement);
            }
        }
        return null;
    }

    private Account buildAccount(Element element) {
        Account account = new Account();
        account.setId(element.elementText("id"));
        account.setName(element.elementText("name"));
        account.setEmail(element.elementText("email"));
        account.setPassword(element.elementText("password"));
        account.setActivated("true".equals(element.elementText("activated")));
        return account;
    }

    public Account updateAccount(Account account) throws AccountPersistException {
        return null;
    }

    public void deleteAccount(String id) throws AccountPersistException {

    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }
}
