import java.net.URLEncoder

URLEncoder.encode("%", "utf-8")

val f = forms.ImgEditForm.form.fill(forms.ImgEditForm.FormData(Some("1"), Some("1"),Some("1"),Some("1")))
f("action").label
f("action").name
f("action").value
f("action").id