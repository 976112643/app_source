package com.wq.support.annotation;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;

/**
 * 视图自动注入
 * @author WQ 下午4:00:04
 */
public class InjectUtility {
	/**
	 * Activity视图注入
	 * 
	 * @param sourceActivity
	 */
	public static void initInjectedView(Activity sourceActivity) {
		ViewInject inject = sourceActivity.getClass().getAnnotation(ViewInject.class);
		if (inject != null) {
			sourceActivity.getWindow().setContentView(sourceActivity.getLayoutInflater().inflate(inject.value(), null));
		}
		initInjectedView(sourceActivity, sourceActivity.getWindow().getDecorView());
	}

	/**
	 * Fragment视图注入
	 * 
	 * @param fragment
	 * @param defLayout
	 * @return
	 */
	public static View initInjectView(Fragment fragment, int defLayout) {
		ViewInject inject = fragment.getClass().getAnnotation(ViewInject.class);
		LayoutInflater inflater = fragment.getActivity().getLayoutInflater();
		View view = null; 
		if (inject != null) {
			view = inflater.inflate(inject.value(), null);
		} else view = inflater.inflate(defLayout, null);
		initInjectedView(fragment, view);
		return view;
	}

	public static void initInjectedView(Object injectedSource, View sourceView) {
		Class<?> clazz = injectedSource.getClass();
		for (; clazz != Object.class; clazz = clazz.getSuperclass()) {// 循环遍历子类及父类
			Field[] fields = clazz.getDeclaredFields();
			Method[] methods = clazz.getDeclaredMethods();
			if (methods != null) {
				for (Method method : methods) {
					Click click = method.getAnnotation(Click.class);
					if (click != null && click.value() != -1) {
						setViewClickListener(injectedSource, sourceView.findViewById(click.value()), method.getName());
					}
				}
			}
			if (fields != null && fields.length > 0) {
				for (Field field : fields) {
					ViewInject viewInject = field.getAnnotation(ViewInject.class);
					if (viewInject != null) {
						// ViewId可以是id配置，也可以是IdStr配置
						int viewId = viewInject.value();
						if (viewId == -1) {
							String idStr = field.getName();
							if (!TextUtils.isEmpty(idStr)) {
								try {
									Context context = sourceView.getContext();
									String packageName = context.getPackageName();
									Resources resources = context.getPackageManager().getResourcesForApplication(packageName);
									viewId = resources.getIdentifier(idStr, "id", packageName);

									if (viewId == 0)
										throw new RuntimeException(String.format("%s 的属性%s关联了id=%s，但是这个id是无效的", clazz.getSimpleName(), field.getName(), idStr));
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
						if (viewId != -1) {
							try {
								field.setAccessible(true);
								/*
								 * 当已经被赋值时，不在重复赋值，用于include，inflate情景下的viewinject组合
								 */
								if (field.get(injectedSource) == null) {
									field.set(injectedSource, sourceView.findViewById(viewId));
								} else {
									continue;
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						bindEvent(injectedSource, field, viewInject);
					}
				}
			}
		}

	}

	private static void bindEvent(Object injectedSource, Field field, ViewInject viewInject) {
		String clickMethod = viewInject.click();
		if (!TextUtils.isEmpty(clickMethod))
			setViewClickListener(injectedSource, field, clickMethod);

		String longClickMethod = viewInject.longClick();
		if (!TextUtils.isEmpty(longClickMethod))
			setViewLongClickListener(injectedSource, field, longClickMethod);

		String itemClickMethod = viewInject.itemClick();
		if (!TextUtils.isEmpty(itemClickMethod))
			setItemClickListener(injectedSource, field, itemClickMethod);

		String itemLongClickMethod = viewInject.itemLongClick();
		if (!TextUtils.isEmpty(itemLongClickMethod))
			setItemLongClickListener(injectedSource, field, itemLongClickMethod);
	}

	public static void setViewClickListener(Object injectedSource, Field field, String clickMethod) {
		try {
			Object obj = field.get(injectedSource);
			if (obj instanceof View) {
				((View) obj).setOnClickListener(new EventListener(injectedSource).click(clickMethod));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void setViewClickListener(Object injectedSource, View obj, String clickMethod) {
		try {
			((View) obj).setOnClickListener(new EventListener(injectedSource).click(clickMethod));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void setViewLongClickListener(Object injectedSource, Field field, String clickMethod) {
		try {
			Object obj = field.get(injectedSource);
			if (obj instanceof View) {
				((View) obj).setOnLongClickListener(new EventListener(injectedSource).longClick(clickMethod));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void setItemClickListener(Object injectedSource, Field field, String itemClickMethod) {
		try {
			Object obj = field.get(injectedSource);
			if (obj instanceof AbsListView) {
				((AbsListView) obj).setOnItemClickListener(new EventListener(injectedSource).itemClick(itemClickMethod));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void setItemLongClickListener(Object injectedSource, Field field, String itemClickMethod) {
		try {
			Object obj = field.get(injectedSource);
			if (obj instanceof AbsListView) {
				((AbsListView) obj).setOnItemLongClickListener(new EventListener(injectedSource).itemLongClick(itemClickMethod));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void setViewSelectListener(Object injectedSource, Field field, String select, String noSelect) {
		try {
			Object obj = field.get(injectedSource);
			if (obj instanceof View) {
				((AbsListView) obj).setOnItemSelectedListener(new EventListener(injectedSource).select(select).noSelect(noSelect));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
