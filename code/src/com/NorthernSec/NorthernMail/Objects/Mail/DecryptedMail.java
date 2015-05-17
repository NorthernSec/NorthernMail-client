package com.NorthernSec.NorthernMail.Objects.Mail;

import com.NorthernSec.NorthernMail.Objects.PrivKey;
import com.NorthernSec.NorthernMail.Objects.PubKey;

public class DecryptedMail {
	private String subject;
	private String message;
	private PubKey signedBy;
	private PrivKey receiver;
    private Boolean wasEncrypted;
    
    public DecryptedMail(String subj, String mess, PubKey signedBy, PrivKey receiver, Boolean encrypted){
    	subject=subj;message=mess;this.signedBy=signedBy;this.receiver=receiver;wasEncrypted=encrypted;
    }
    public String getSubject(){return subject;}
    public String getMessage(){return message;}
    public String getSender(){return signedBy.getName();}
    public PubKey getSenderKey(){return signedBy;}
    public String getReceiver(){return receiver.getName();}
    public PrivKey getReveiverKey(){return receiver;}
    public Boolean wasEncrypted(){return wasEncrypted;}
}
