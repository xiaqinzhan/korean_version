/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: E:\\android_project_kr\\Reminder\\src\\com\\jason\\reminder\\ReminderCaller.aidl
 */
package com.jason.reminder;
public interface ReminderCaller extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.jason.reminder.ReminderCaller
{
private static final java.lang.String DESCRIPTOR = "com.jason.reminder.ReminderCaller";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.jason.reminder.ReminderCaller interface,
 * generating a proxy if needed.
 */
public static com.jason.reminder.ReminderCaller asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = (android.os.IInterface)obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.jason.reminder.ReminderCaller))) {
return ((com.jason.reminder.ReminderCaller)iin);
}
return new com.jason.reminder.ReminderCaller.Stub.Proxy(obj);
}
public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_updateConfig:
{
data.enforceInterface(DESCRIPTOR);
this.updateConfig();
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.jason.reminder.ReminderCaller
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
public void updateConfig() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_updateConfig, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_updateConfig = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
}
public void updateConfig() throws android.os.RemoteException;
}
