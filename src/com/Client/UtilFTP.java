package com.Client;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.enterprisedt.net.ftp.FTPClient;
import com.enterprisedt.net.ftp.FTPException;
import com.enterprisedt.net.ftp.FTPTransferType;
import com.enterprisedt.net.ftp.FileTransferClient;
import com.enterprisedt.net.ftp.WriteMode;

public class UtilFTP {

	/**
	 * 检查FTP服务器上文件夹是否存在
	 * 
	 * @param pFolder
	 *            FTP上对于根目录的路径
	 * @throws Exception
	 */
	private static boolean checkFolderIsExist(boolean isLogin,
			FTPClient ftpClient, String pFolder) throws Exception {
		if (isLogin) {
			String folder = pFolder.trim();
			if (folder.startsWith("//")) {
				folder = folder.substring(1);
			}
			if (folder.endsWith("//")) {
				folder = folder.substring(0, folder.length() - 1);
			}
			String strLayer = "..";
			if (folder.indexOf("//") > 0) {
				String[] folders = folder.split("////");
				for (int i = 1; i < folders.length; i++) {
					strLayer += ",";
				}
			}
			boolean result = false;
			try {
				ftpClient.chdir(folder);
				ftpClient.chdir(strLayer);
				result = true;
			} catch (Exception ex) {
				result = false;
			}
			return result;
		} else {
			throw new Exception("ftp没有登录!");
		}
	}

	/**
	 * 创建远程FTP服务器文件夹
	 * 
	 * @param pFolder
	 *            FTP上对于根目录的路径
	 * @throws Exception
	 */
	private static void createFolder(boolean isLogin, FTPClient ftpClient,
			String pFolder) throws Exception {
		if (isLogin) {
			if (checkFolderIsExist(isLogin, ftpClient, pFolder) == false) {
				try {
					String path = "";
					ftpClient.chdir("//");
					String[] folders = pFolder.split("////");
					for (int i = 0; i < folders.length; i++) {
						try {
							ftpClient.chdir(folders[i]);
						} catch (Exception ex) {
							ftpClient.mkdir(folders[i]);
							ftpClient.chdir(folders[i]);
						}
					}
				} catch (Exception ex) {
					throw new Exception(ex.getMessage());
				}
			}
		} else {
			throw new Exception("ftp没有登录!");
		}
	}

	private static boolean connectFtp(FTPClient ftpClient, String ftpServer,
			int ftpHost, String user, String pass, int num) {
		boolean isLogin = false;
		try {
			ftpClient.setRemoteHost(ftpServer);
			ftpClient.setRemotePort(ftpHost);
			ftpClient.setControlEncoding("gbk"); // 加上这一句后在 edtftpj 2.0.1
													// 下就可以传中文文件名了
			System.out.println("开始登录");
			ftpClient.connect();
			ftpClient.login(user, pass);
			System.out.println("登录成功");
			if (num == 64) {
				ftpClient.chdir("//");
			}
			if (num == 32) {
				ftpClient.chdir("/");
			}
			System.out.println("已转入根目录");
			isLogin = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isLogin;
	}

	/**
	 * 连接FTP connect(String ftpServer,int ftpHost,String user,String pass)
	 * 
	 * @param ftpServer
	 *            服务器IP
	 * @param ftpHost
	 *            端口
	 * @param user
	 *            用户名
	 * @param pass
	 *            密码
	 * @throws IOException
	 */
	public static boolean connect(FTPClient ftpClient, String ftpServer,
			int ftpHost, String user, String pass) {
		return connectFtp(ftpClient, ftpServer, ftpHost, user, pass, 64);
	}

	private static String getFtpP(String ml) {
		String[] mls = ml.split("/");
		String fp = "//";
		for (String m : mls) {
			if (!"".equals(m))
				fp = fp + m + "////";
		}
		fp = fp + getRqml(0) + "////";
		return fp;
	}

	/**
	 * 上传单个文件 uploadFile(String clientFileName, String ftpPath)
	 * 
	 * @param clientFileName
	 *            本地要上传的文件的全路径
	 * @param ftpPath
	 *            FTP上对于根目录的路径
	 * @throws IOException
	 */
	public static void uploadFile(boolean isLogin, FTPClient ftpClient,
			String clientFileName, String ml) throws Exception {
		if (isLogin) {
			try {
				String ftpPath = getFtpP(ml);
				// 获取文件名
				String filename = "";
				int index = clientFileName.lastIndexOf("/");
				filename = clientFileName.substring(index + 1);
				ftpClient.chdir("//");
				ftpClient.setType(FTPTransferType.BINARY);
				if (checkFolderIsExist(isLogin, ftpClient, ftpPath)) {
					ftpClient.chdir(ftpPath);
				} else {
					createFolder(isLogin, ftpClient, ftpPath);
				}
				ftpClient.put(clientFileName, filename);
			} catch (Exception ex) {
				throw new Exception(ex.getMessage());
			}
		} else {
			throw new Exception("ftp没有登录!");
		}
	}

	private static boolean checkFolderIsExist32(boolean isLogin,
			FTPClient ftpClient, String pFolder) throws Exception {
		if (isLogin) {
			String folder = pFolder.trim();
			String strLayer = "..";
			if (folder.startsWith("//"))
				folder = folder.substring(1);
			if (folder.endsWith("//"))
				folder = folder.substring(0, folder.length() - 1);
			boolean result = false;
			try {
				ftpClient.chdir(folder);
				result = true;
			} catch (Exception ex) {
				result = false;
			}
			return result;
		} else {
			throw new Exception("ftp没有登录!");
		}
	}

	private static void createFolder32(boolean isLogin, FTPClient ftpClient,
			String pFolder) throws Exception {
		if (isLogin) {
			if (!checkFolderIsExist32(isLogin, ftpClient, pFolder))
				try {
					String ftpPathTEMP = pFolder;
					if (ftpPathTEMP.startsWith("//"))
						ftpPathTEMP = ftpPathTEMP.substring(1);
					if (ftpPathTEMP.endsWith("//"))
						ftpPathTEMP = ftpPathTEMP.substring(0,
								ftpPathTEMP.length() - 1);
					String path = "";
					ftpClient.chdir("/");
					String folders[] = ftpPathTEMP.split("/");
					for (int i = 1; i < folders.length; i++) {
						if (!"".equals(folders[i].toString())
								|| folders[i].toString() != null) {
							try {
								ftpClient.chdir(folders[i]);
							} catch (Exception ex) {
								ftpClient.mkdir(folders[i]);
								ftpClient.chdir(folders[i]);
							}
						}
					}
				} catch (Exception ex) {
					throw new Exception(ex.getMessage());
				}
		} else {
			throw new Exception("ftp没有登录!");
		}
	}

	public static boolean connect32(FTPClient ftpClient, String ftpServer,
			int ftpHost, String user, String pass) {
		return connectFtp(ftpClient, ftpServer, ftpHost, user, pass, 32);
	}

	private static String getFtpP32(String ml) {
		String[] mls = ml.split("/");
		String fp = "//";
		for (String m : mls) {
			if (!"".equals(m))
				fp = fp + m + "/";
		}
		fp = fp + getRqml(0) + "//";
		return fp;
	}

	public static void uploadFile32(boolean isLogin, FTPClient ftpClient,
			String clientFileName, String ml) throws Exception {
		if (isLogin) {
			try {
				String ftpPath = getFtpP32(ml);
				String filename = "";
				int index = clientFileName.lastIndexOf("/");
				filename = clientFileName.substring(index + 1);
				ftpClient.chdir("/");
				ftpClient.setType(FTPTransferType.BINARY);
				String ftpPathTEMP = ftpPath;
				if (ftpPathTEMP.startsWith("//"))
					ftpPathTEMP = ftpPathTEMP.substring(1);
				if (ftpPathTEMP.endsWith("//"))
					ftpPathTEMP = ftpPathTEMP.substring(0,
							ftpPathTEMP.length() - 1);
				if (checkFolderIsExist32(isLogin, ftpClient, ftpPath)) {
					ftpClient.chdir(ftpPathTEMP);
				} else {
					createFolder32(isLogin, ftpClient, ftpPath);
				}
				ftpClient.put(clientFileName, filename);
			} catch (Exception ex) {
				throw new Exception(ex.getMessage());
			}
		} else
			throw new Exception("ftp没有登录!");
	}

	public static String uploadFtp(Map map, String rp, String ml, List<Map> nn,
			int num, int port, String name, String pass) {
		// 上传ftp服务器
		String tp = (String) map.get("tp");
		String tpdk = (String) map.get("tpdk");
		String tpserver = tp.substring(7, tp.length());
		FTPClient ftpClient = new FTPClient();
		boolean b = false;
		String nnn = "";
		try {
			if (num == 32)
				b = UtilFTP.connect32(ftpClient, tpserver, port, name, pass);
			else
				b = UtilFTP.connect(ftpClient, tpserver, port, name, pass);
			if (b) {
				for (Map sp : nn) {
					System.out.println("准备FTP上传" + rp + sp.get("filepath"));
					if (num == 32)
						UtilFTP.uploadFile32(b, ftpClient,
								rp + sp.get("filepath"), ml);
					else
						UtilFTP.uploadFile(b, ftpClient,
								rp + sp.get("filepath"), ml);
					System.out.println("FTP上传成功");
				}
				for (Map pn : nn) {
					nnn = nnn + tpdk + '/' + ml + '/' + getRqml(0)
							+ "/" + pn.get("filepath") + ",";
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return nnn;
	}

	

	/**
	 * 下载文件
	 * 
	 * @param localFileName
	 *            本地文件名
	 * @param remoteFileName
	 *            远程文件名
	 * @param tpserver ip
	 * @param port 端口
	 * @param name 用户名
	 * @param pass 密码
	 * @throws FTPException
	 * @throws IOException
	 */
	public void ftpDownload(String localFileName, String remoteFileName,
			String tpserver, int port, String name, String pass)
			throws FTPException, IOException {
		FileTransferClient ftpClient = new FileTransferClient();
		ftpClient = new FileTransferClient();
		ftpClient.setUserName(name);
		ftpClient.setPassword(pass);
		ftpClient.setRemoteHost(tpserver);
		ftpClient.setRemotePort(port);
		ftpClient.setTimeout(1200);
		ftpClient.getAdvancedSettings().setTransferBufferSize(1000);
		ftpClient.getAdvancedSettings().setTransferNotifyInterval(5000);
		ftpClient.getAdvancedSettings().setControlEncoding("GBK");
		ftpClient.connect();
		ftpClient.downloadFile(localFileName, remoteFileName,WriteMode.OVERWRITE);
	}
	/**
	 * 上传文件
	 * @param localFilePath 本地路径
	 * @param remoteFilePath 远程路径
	 * @param tpserver ip
	 * @param port 端口
	 * @param name 用户名名
	 * @param pass 密码
	 * @throws FTPException
	 * @throws IOException
	 */
    public void ftpUploadFile(String localFilePath, String remoteFilePath,String tpserver, int port, String name, String pass) 
    		throws FTPException, IOException { 
    			File file = new File(localFilePath);
            	FileTransferClient ftpClient = new FileTransferClient();
        		ftpClient = new FileTransferClient();
        		ftpClient.setUserName(name);
        		ftpClient.setPassword(pass);
        		ftpClient.setRemoteHost(tpserver);
        		ftpClient.setRemotePort(port);
        		ftpClient.setTimeout(1200);
        		ftpClient.getAdvancedSettings().setTransferBufferSize(1000);
        		ftpClient.getAdvancedSettings().setTransferNotifyInterval(5000);
        		ftpClient.getAdvancedSettings().setControlEncoding("GBK");
        		ftpClient.connect();
        		ftpClient.uploadFile(localFilePath, remoteFilePath, WriteMode.OVERWRITE); 
             
    } 
    /**
     * 删除指定文件 
     * @param ftpPath
     * @param tpserver
     * @param port
     * @param name
     * @param pass
     * @return
     * @throws Exception
     */
    public void deleteFile(String ftpPath,String tpserver, int port, String name, String pass) throws FTPException, IOException {
	    	FileTransferClient ftpClient = new FileTransferClient();
			ftpClient = new FileTransferClient();
			ftpClient.setUserName(name);
			ftpClient.setPassword(pass);
			ftpClient.setRemoteHost(tpserver);
			ftpClient.setRemotePort(port);
			ftpClient.setTimeout(1200);
			ftpClient.getAdvancedSettings().setTransferBufferSize(1000);
			ftpClient.getAdvancedSettings().setTransferNotifyInterval(5000);
			ftpClient.getAdvancedSettings().setControlEncoding("GBK");
			ftpClient.connect();
			ftpClient.deleteFile(ftpPath);
    }
    
    public static String getRqml(int f) {
		String ft = "yyyyMMdd";
		if (f == 1)
			ft = "yyyyMMddHHmmss";
		return new SimpleDateFormat(ft).format(new Date());
	}
	public static String uploadFtp(Map map, String rp, String ml, List<Map> nn,
			int num) {
		return uploadFtp(map, rp, ml, nn, num, 21, "tp", "tp");
	}
}
